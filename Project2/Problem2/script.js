// ===========================
// Vanilla JavaScript DOM Manipulations
// ===========================

// Change the text using innerHTML
document.getElementById("changeTextBtn").onclick = function() {
    document.getElementById("myHeading").innerHTML = "Heading Changed!";
};

// Change CSS properties like color and position
document.getElementById("changeColorBtn").onclick = function() {
    document.getElementById("myHeading").style.color = "red";
};

document.getElementById("moveHeadingBtn").onclick = function() {
    let heading = document.getElementById("myHeading");
    heading.style.position = "relative";
    heading.style.left = "50px";
};

// Change the image source after clicking a button
document.getElementById("changeImageBtn").onclick = function() {
    document.getElementById("myImage").src = "https://via.placeholder.com/200/ff0000/ffffff?text=New+Image";
};

// Add a text node and attach it to a parent node
document.getElementById("addTextNodeBtn").onclick = function() {
    let newText = document.createTextNode("This is a dynamically added text.");
    let container = document.getElementById("container");
    container.appendChild(newText);
};

// Delete a node
document.getElementById("deleteNodeBtn").onclick = function() {
    let paragraphs = document.getElementsByClassName("myClass");
    if(paragraphs.length > 0) {
        paragraphs[0].parentNode.removeChild(paragraphs[0]);
    }
};

// ===========================
// jQuery Operations
// ===========================

$(document).ready(function() {
    // Change button text using jQuery
    $("#changeTextBtn").text("Click to Change Heading Text");

    // Set background-image using jQuery CSS property
    $("#container").css("background-image", "url('https://via.placeholder.com/400x100')");
    $("#container").css("background-size", "cover");

    // Access HTML form data using jQuery
    $("#myForm").submit(function(event) {
        event.preventDefault(); // prevent form submission
        let name = $("#nameInput").val();
        let age = $("#ageInput").val();
        alert("Name: " + name + "\nAge: " + age);
    });

    // Add attribute using jQuery
    $("#myImage").attr("title", "This is a dynamically added title");
});
