# File Encrypter

A simple file encrypter/decrypter built in Java.  
This application allows you to encrypt and decrypt files using AES-256 encryption, with a graphical user interface (JavaFX).

## Features

- Encrypt any file using AES-256
- Decrypt encrypted files back to their original format
- Simple and intuitive UI (JavaFX)
- Cross-platform support

## Prerequisites

- **Java 17** or newer (Java 21 recommended)
- **JavaFX SDK 17+** ([Download here](https://gluonhq.com/products/javafx/))
  - Download and extract the SDK (e.g., to `C:\javafx-sdk-21.0.8`)

## Setup & Usage

1. **Clone this repository**
   ```
   git clone https://github.com/cyrolite/file-encrypter.git
   ```

2. **Download and extract JavaFX SDK**  
   [JavaFX SDK Download](https://gluonhq.com/products/javafx/)

3. **Edit `compileAndRun.bat` if needed**  
   - Make sure the path to your JavaFX SDK in the script matches your setup.

4. **Build and run the application**
   ```
   compileAndRun.bat
   ```

   This will compile and launch the application automatically.

## Manual Compile/Run (if not using the batch file)

If you prefer to run commands manually:

```
javac --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp src/main/resources -d out src/main/java/io/*.java src/main/java/tools/*.java src/main/java/util/*.java
java --module-path "path/to/javafx-sdk/lib" --add-modules javafx.controls,javafx.fxml -cp out;src/main/resources io.Main
```

Replace `"path/to/javafx-sdk/lib"` with the actual path to your JavaFX SDK's `lib` folder.

## Notes

- The encrypted file will be saved as `encrypt.txt` in your chosen output directory.
- The decrypted file will be saved as `decrypt.<original_extension>` in the same directory.
- Make sure your secret key and salt are kept safe for decryption.

---

**If you encounter any issues, please open an issue or pull request.**