alert('hello world-----Good morning');
name = prompt("Enter your name");
document.write("Hi " + name + "<br>");
document.write("Hello " + name);

n1 = Number(prompt("Enter number"));
n2 = Number(prompt("Enter number"));
document.write("The answer is " + (n1 + n2) + "br");

function multiplicationTable(num) {
    i = 1;
    while (i <= 20) {
        document.write(num + " x " + i + "=" + (num * i) + "<br>");
        i++;
    }
}

function power(num, exp) {
    return num ** exp;
}

num = Number(prompt("Enter number whose table you want to print"));
multiplicationTable(num);

num1 = Number(prompt("Enter number <br>"));
exp = Number(prompt("Enter the exponent"));
document.write(num1 + "^" + exp + "= " + power(num1, exp) + "<br>");

arr = [29, "om", "Helo", 1012];
document.write(arr);

book = [];
document.write("Enter 5 books<br>");
for (i = 0; i < 5; i++) {
    book.push(prompt("Enter book<br>"));
}

elem = prompt("enter book you want to search<br>");
f = 0;
for (i = 0; i < 5; i++) {
    if (book[i] == elem) {
        f = 1;
        break;
    }
}

if (f == 1) {
    document.write("The book is present<br>");
}
else {
    document.write("The book is not present<br>");
}