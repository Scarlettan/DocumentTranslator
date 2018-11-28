# DocumentTranslator
Software that translates files and documents

###### Supported Files
- .xlsx
- .docx
- .pptx
- .ods

###### Known Issues
The generated output file may show errors when opening through their default programs. It could either be caused by:
- Character Limits (esp. in sheet names), as the translated words/sentences can be far longer than the original
- Broken Spreadsheet Formulas, this can be caused by the spacing between the symbols after translation
For the most part, the errors can be ignored unless the file is rendered unreadable. Note that this translation tool is intended only for reference, and does not guarantee perfect translation of the source material.

###### Disclaimer
This is a pet project I did to make translating documents at my work easier, so at the moment it only supports 日本語. I may or may not include more language support in the future, as well as other file format support.

I also did not acquire/use any specific translation service, so the mechanism is manual in that the program only extracts the terms. The user will have to use a service like Google Translate to get translations and input them in the program.
