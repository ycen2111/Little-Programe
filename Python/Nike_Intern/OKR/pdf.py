import pdfplumber

pdf=pdfplumber.open(r"D:\Python\Intern\data\OKR SCI FY23 Aug2022 v1.pdf")

first_page = pdf.pages[0]

#list=""
#for char in first_page.chars:
    #list+=char["text"]
    #list+=" "

print(first_page.chars[7])