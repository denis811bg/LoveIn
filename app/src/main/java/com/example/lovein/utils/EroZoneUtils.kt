package com.example.lovein.utils

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import com.example.lovein.common.constants.CommonConstants
import com.example.lovein.common.constants.ExceptionConstants
import com.example.lovein.common.constants.PDFDocumentConstants
import com.example.lovein.common.data.EroZone
import com.example.lovein.common.models.EroZoneMutable
import com.example.lovein.common.models.FeedbackType
import com.example.lovein.common.models.Partner
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.erozoneexplorer.models.Card

enum class Dest { Downloads, Documents }

data class PdfSaveResult(
    val uri: Uri,
    val directoryLabel: String,
    val filename: String
)

fun convertEroZoneToEroZoneMutable(eroZone: EroZone): EroZoneMutable {
    return EroZoneMutable(
        gender = eroZone.gender,
        resourceId = eroZone.resourceId,
        eroZoneType = eroZone.eroZoneType,
        actionList = eroZone.actionList.toMutableList()
    )
}

fun convertEroZoneMutableListToEroZoneList(selectedEroZoneMutableList: SnapshotStateList<EroZoneMutable>): List<EroZone> {
    return selectedEroZoneMutableList.mapNotNull { eroZoneMutable ->
        EroZone.entries.find { eroZone -> eroZone.resourceId == eroZoneMutable.resourceId }
    }
}

fun convertEroZoneListToEroZoneMutableList(eroZoneList: List<EroZone>): SnapshotStateList<EroZoneMutable> {
    return eroZoneList.map { eroZone ->
        EroZoneMutable(
            gender = eroZone.gender,
            resourceId = eroZone.resourceId,
            eroZoneType = eroZone.eroZoneType,
            actionList = eroZone.actionList.toMutableList()
        )
    }.toMutableStateList()
}

fun generateFeedbackReport(
    context: Context,
    actionCards: List<Pair<Partner, Card>>
): Map<String, Map<String, List<Pair<String, FeedbackType>>>> {
    val report = mutableMapOf<String, MutableMap<String, MutableList<Pair<String, FeedbackType>>>>()

    actionCards.forEach { (partner, card) ->
        val partnerName = partner.name.value
        val action = card.front.actionWithFeedback.action

        val eroZoneName = LocalizationManager.getLocalizedString(context, action.nameResId)
        val actionDescription =
            LocalizationManager.getLocalizedString(context, action.descriptionResId)
        val feedback = card.front.actionWithFeedback.feedback.value

        val partnerReport = report.getOrPut(partnerName) { mutableMapOf() }
        val zoneActions = partnerReport.getOrPut(eroZoneName) { mutableListOf() }

        zoneActions.add(actionDescription to feedback)
    }

    return report
}

fun saveFeedbackReportToPdf(
    context: Context,
    report: Map<String, Map<String, List<Pair<String, FeedbackType>>>>,
    dest: Dest = Dest.Downloads
): PdfSaveResult {
    val pdfDocument = PdfDocument()

    val paint = android.graphics.Paint().apply {
        textSize = 14f
        color = android.graphics.Color.BLACK
    }

    var y = PDFDocumentConstants.MARGIN_TOP
    var pageIndex = 1
    var page = pdfDocument.startPage(
        PdfDocument.PageInfo.Builder(
            PDFDocumentConstants.PAGE_WIDTH,
            PDFDocumentConstants.PAGE_HEIGHT,
            pageIndex
        ).create()
    )
    var canvas = page.canvas

    fun newPage() {
        pdfDocument.finishPage(page)
        pageIndex++
        page = pdfDocument.startPage(
            PdfDocument.PageInfo.Builder(
                PDFDocumentConstants.PAGE_WIDTH,
                PDFDocumentConstants.PAGE_HEIGHT,
                pageIndex
            ).create()
        )
        canvas = page.canvas
        y = PDFDocumentConstants.MARGIN_TOP
    }

    report.forEach { (partnerName, zones) ->
        if (y + PDFDocumentConstants.LINE_HEIGHT > PDFDocumentConstants.PAGE_HEIGHT) newPage()
        canvas.drawText("👤 Partner: $partnerName", 20f, y, paint)
        y += PDFDocumentConstants.LINE_HEIGHT

        zones.forEach { (eroZoneName, actions) ->
            if (y + PDFDocumentConstants.LINE_HEIGHT > PDFDocumentConstants.PAGE_HEIGHT) newPage()
            canvas.drawText("   🔸 EroZone: $eroZoneName", 30f, y, paint)
            y += PDFDocumentConstants.LINE_HEIGHT

            actions.forEach { (actionDescription, feedback) ->
                if (y + PDFDocumentConstants.LINE_HEIGHT > PDFDocumentConstants.PAGE_HEIGHT) newPage()
                canvas.drawText("      • $actionDescription — ${feedback.name}", 40f, y, paint)
                y += PDFDocumentConstants.LINE_HEIGHT
            }

            y += PDFDocumentConstants.EXTRA_SPACING
        }

        y += PDFDocumentConstants.EXTRA_SPACING * 2
    }

    pdfDocument.finishPage(page)

    val filename = "ero_zone_feedback_report_${
        java.text.SimpleDateFormat(CommonConstants.TIMESTAMP_PATTERN, java.util.Locale.getDefault())
            .format(java.util.Date())
    }.pdf"
    val dirLabel = if (dest == Dest.Downloads) "Downloads" else "Documents"

    val uri = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        val values = android.content.ContentValues().apply {
            put(android.provider.MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(
                android.provider.MediaStore.MediaColumns.RELATIVE_PATH,
                if (dest == Dest.Downloads)
                    android.os.Environment.DIRECTORY_DOWNLOADS
                else
                    android.os.Environment.DIRECTORY_DOCUMENTS
            )
        }

        val collectionUri = if (dest == Dest.Downloads)
            android.provider.MediaStore.Downloads.EXTERNAL_CONTENT_URI
        else
            android.provider.MediaStore.Files.getContentUri(android.provider.MediaStore.VOLUME_EXTERNAL_PRIMARY)

        val resolver = context.contentResolver
        val itemUri = resolver.insert(collectionUri, values)
            ?: throw java.io.IOException(ExceptionConstants.CREATE_PDF_FILE_FAILED)

        resolver.openOutputStream(itemUri)?.use { os ->
            pdfDocument.writeTo(os)
        } ?: throw java.io.IOException(ExceptionConstants.OPEN_OUTPUT_STREAM_FAILED)

        pdfDocument.close()
        itemUri
    } else {
        val dir = android.os.Environment.getExternalStoragePublicDirectory(
            if (dest == Dest.Downloads)
                android.os.Environment.DIRECTORY_DOWNLOADS
            else
                android.os.Environment.DIRECTORY_DOCUMENTS
        )
        if (!dir.exists()) dir.mkdirs()

        val file = java.io.File(dir, filename)
        java.io.FileOutputStream(file).use { fos ->
            pdfDocument.writeTo(fos)
        }
        pdfDocument.close()

        android.media.MediaScannerConnection.scanFile(
            context, arrayOf(file.absolutePath), arrayOf("application/pdf"), null
        )

        androidx.core.content.FileProvider.getUriForFile(
            context, "${context.packageName}.provider", file
        )
    }

    return PdfSaveResult(uri = uri, directoryLabel = dirLabel, filename = filename)
}

