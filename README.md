# File Encrypter

A simple file encrypter/decrypter built in Java.  
This application allows you to encrypt and decrypt files using AES-256 encryption, with a graphical user interface (JavaFX) or console interface.

## Features

- Encrypt any file using AES-256
- Decrypt encrypted files back to their original format
- Simple and intuitive UI (JavaFX)
- Cross-platform support

## Prerequisites

- **Java 17** or newer
- **JavaFX SDK 17+** ([Download here](https://gluonhq.com/products/javafx/))

## Setup

1. **Download and extract JavaFX SDK**  
   [JavaFX SDK Download](https://gluonhq.com/products/javafx/)

2. **Clone this repository**
   ```
   git clone https://github.com/yourusername/file-encrypter.git
   ```

3. **Compile the project**
   ```
   javac --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -d out src/main/java/io/*.java src/main/java/tools/*.java src/main/java/util/*.java
   ```

4. **Run the application**
   ```
   java --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp out io.Main
   ```

   Replace `"path/to/javafx-sdk/lib"` with the actual path to your JavaFX SDK's `lib` folder.

## Usage

1. Launch the application.
2. Select the file you want to encrypt.
3. Choose the output directory.
4. Enter your secret key and salt.
5. Click "Encrypt and Decrypt" to process the file.

## Notes

- The encrypted file will be saved as `encrypt.txt` in your chosen output directory.
- The decrypted file will be saved as `decrypt.<original_extension>` in the same directory.
- Make sure your secret key and salt are kept safe for decryption.

---

**If you encounter any issues, please open an issue