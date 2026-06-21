import os
from reportlab.platypus import (
    SimpleDocTemplate,
    Paragraph,
    Spacer
)
from reportlab.lib.styles import getSampleStyleSheet

def generate_pdf(report:dict):
    os.makedirs(
        "reports",
        exist_ok=True
    )

    alert_id = report["alertId"]
    pdf_path=f"reports/report_{alert_id}.pdf"
    document = SimpleDocTemplate(pdf_path)
    styles = getSampleStyleSheet()
    content = []
    content.append(
        Paragraph(
            "Fraud Investigation Report",
            styles["Title"]
        )
    )
    content.append(Spacer(1,20))
    content.append(
        Paragraph(
            f"<b>Alert ID:</b> {report['transactionId']}",
            styles["Normal"]
        )
    )
    content.append(
        Paragraph(
            f"<b>Risk Level:</b> {report['riskLevel']}",
            styles["Normal"]
        )
    )

    content.append(
        Spacer(1, 20)
    )

    content.append(
        Paragraph(
            "<b>Summary</b>",
            styles["Heading2"]
        )
    )

    content.append(
        Paragraph(
            report["summary"],
            styles["BodyText"]
        )
    )

    content.append(
        Spacer(1, 20)
    )

    content.append(
        Paragraph(
            "<b>Recommendation</b>",
            styles["Heading2"]
        )
    )

    content.append(
        Paragraph(
            report["recommendation"],
            styles["BodyText"]
        )
    )

    document.build(content)

    return pdf_path
