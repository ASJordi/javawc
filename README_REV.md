# java-wc
java-wc is a Java utility designed to replicate the functionality of the well-known Unix command-line tool, wc. This tool counts the number of words, lines, and bytes within a specified file or from standard input. Additionally, it provides features such as word frequency and identification of the most repeated word in the file.

https://github.com/ShravanthReddy/java-wc/assets/32842412/317a5533-4544-4616-93f3-f364c32f2353

### Features
- Counts words, lines, and bytes similar to the popular Unix tool 'wc'.
- Supports file input using the file path as an argument or reading from standard input via the cat command.
- Provides options to output word frequency and to identify the most repeated word in the file.

### Getting Started

Follow the steps below to get started:

1. Clone the repository using Git:

   ```bash
   git clone https://github.com/ShravanthReddy/java-wc.git
   ```

2. Change to the project directory:

   ```bash
   cd java-wc/src
   ```

3. Compile and run:

   Ensure that JDK is installed on your system for the following steps to work.

   ```bash
   javac Wc.java
   java Wc [flags] [filename]
   ```

### Usage
```bash
java Wc [flags] [filename] 

Flags:
  -h,	Displays help for 'java-wc'
  -c,	Outputs the numbers of bytes in the file
  -l,	Outputs the numbers of lines in the file
  -w,	Outputs the numbers of words in the file
  -f	Outputs the frequency of each word in the file
  -m,	Outputs the most repeated word and its count
```

To view help information, type the following command in the terminal:
```bash
java Wc -h
```
By default, java Wc [filename] outputs the number of words, lines, bytes, and characters for the file. You Utilize the -c flag to display only bytes, and -w and -l to focus on words or lines respectively. Use -f for each word frequency and -m for identifying the most repeated word in the file.

Example: Get the number of words of a file `test.txt`
```bash
java Wc -w test.txt
```

Example 2: Get the total number of files in a folder

```bash
ls -l | grep -v '^total' | java Wc -l
```