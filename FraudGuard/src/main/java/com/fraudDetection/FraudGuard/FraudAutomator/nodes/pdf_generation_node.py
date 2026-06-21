from pdf.pdf_generator import generate_pdf

def pdf_generation_node(state):
    pdf_path = generate_pdf(state["report"])
    print(f"PDF Generated: {pdf_path}")
    return {
        "pdf_path":pdf_path
    }