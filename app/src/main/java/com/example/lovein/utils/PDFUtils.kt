package com.example.lovein.utils

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.lovein.R
import com.example.lovein.common.constants.CommonConstants
import com.example.lovein.common.constants.ExceptionConstants
import com.example.lovein.common.constants.PDFDocumentConstants
import com.example.lovein.common.data.Dest
import com.example.lovein.common.models.FeedbackType
import com.example.lovein.common.models.Partner
import com.example.lovein.common.objects.LocalizationManager
import com.example.lovein.erozoneexplorer.models.Card
import java.io.File
import java.io.FileOutputStream
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FILE_UNSAFE = Regex("""[\\/:*?"<>|]+""")

data class PdfSaveResult(
    val uri: Uri, val filename: String
)

private fun buildReportFileName(context: Context): String {
    val base = LocalizationManager.getLocalizedString(context, R.string.body_map_and_ratings).trim()
        .replace("\\s+".toRegex(), "_").replace(FILE_UNSAFE, "")

    val ts: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val fmt = DateTimeFormatter.ofPattern(
            CommonConstants.TIMESTAMP_PATTERN, Locale.getDefault()
        )
        java.time.LocalDateTime.now().format(fmt)
    } else {
        val sdf = java.text.SimpleDateFormat(
            CommonConstants.TIMESTAMP_PATTERN, Locale.getDefault()
        )
        sdf.format(java.util.Date())
    }

    return "${base}_${ts}.pdf"
}

private fun savePdfToPublicDir(
    context: Context, pdf: PdfDocument, fileName: String, dest: Dest
): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, dest.relativePath)
        }

        val collectionUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI

        val resolver = context.contentResolver
        val itemUri = resolver.insert(collectionUri, values) ?: throw java.io.IOException(
            ExceptionConstants.CREATE_PDF_FILE_FAILED
        )

        resolver.openOutputStream(itemUri)?.use { os ->
            pdf.writeTo(os)
        } ?: throw java.io.IOException(ExceptionConstants.OPEN_OUTPUT_STREAM_FAILED)

        itemUri
    } else {
        @Suppress("DEPRECATION") val dir =
            Environment.getExternalStoragePublicDirectory(dest.relativePath)
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, fileName)
        FileOutputStream(file).use { fos -> pdf.writeTo(fos) }

        android.media.MediaScannerConnection.scanFile(
            context, arrayOf(file.absolutePath), arrayOf("application/pdf"), null
        )

        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }
}

fun generateFeedbackReport(
    context: Context, actionCards: List<Pair<Partner, Card>>
): Map<String, Map<String, List<Pair<String, FeedbackType>>>> {
    val report = mutableMapOf<String, MutableMap<String, MutableList<Pair<String, FeedbackType>>>>()

    actionCards.forEach { (partner, card) ->
        val partnerName = partner.name.value
        val action = card.front.actionWithFeedback.action

        val eroZoneName = LocalizationManager.getLocalizedString(context, action.nameResId)
        val actionDescription = card.front.content.text
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
    val pdf = PdfDocument()
    val paint = android.graphics.Paint().apply {
        textSize = 14f
        color = android.graphics.Color.BLACK
    }

    var y = PDFDocumentConstants.MARGIN_TOP
    var pageIndex = 1
    var page = pdf.startPage(
        PdfDocument.PageInfo.Builder(
            PDFDocumentConstants.PAGE_WIDTH, PDFDocumentConstants.PAGE_HEIGHT, pageIndex
        ).create()
    )
    var canvas = page.canvas

    fun newPage() {
        pdf.finishPage(page)
        pageIndex++
        page = pdf.startPage(
            PdfDocument.PageInfo.Builder(
                PDFDocumentConstants.PAGE_WIDTH, PDFDocumentConstants.PAGE_HEIGHT, pageIndex
            ).create()
        )
        canvas = page.canvas
        y = PDFDocumentConstants.MARGIN_TOP
    }

    fun needNewLine() = y + PDFDocumentConstants.LINE_HEIGHT > PDFDocumentConstants.PAGE_HEIGHT

    report.forEach { (partnerName, zones) ->
        if (needNewLine()) newPage()
        canvas.drawText("👤 : $partnerName", 20f, y, paint)
        y += PDFDocumentConstants.LINE_HEIGHT

        zones.forEach { (eroZoneName, actions) ->
            if (needNewLine()) newPage()
            canvas.drawText("   🔸 : $eroZoneName", 30f, y, paint)
            y += PDFDocumentConstants.LINE_HEIGHT

            actions.forEach { (actionDescription, feedback) ->
                if (needNewLine()) newPage()
                canvas.drawText("      • $actionDescription — ${feedback.name}", 40f, y, paint)
                y += PDFDocumentConstants.LINE_HEIGHT
            }
            y += PDFDocumentConstants.EXTRA_SPACING
        }
        y += PDFDocumentConstants.EXTRA_SPACING * 2
    }

    pdf.finishPage(page)

    val fileName = buildReportFileName(context)
    val uri = try {
        savePdfToPublicDir(context, pdf, fileName, dest)
    } finally {
        pdf.close()
    }

    return PdfSaveResult(uri = uri, filename = fileName)
}

fun openPdf(context: Context, uri: Uri) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        val fallback = Intent(Intent.ACTION_VIEW).apply {
            data = uri
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try {
            context.startActivity(fallback)
        } catch (_: Exception) {
            Toast.makeText(context, context.getString(R.string.no_pdf_viewer), Toast.LENGTH_LONG)
                .show()
        }
    }
}