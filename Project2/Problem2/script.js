// ===========================
// Vanilla JavaScript DOM Manipulations
// ===========================

// Change the heading text
document.getElementById("changeTextBtn").onclick = function() {
    document.getElementById("myHeading").innerHTML = "Heading Changed!";
};

// Change color of heading
document.getElementById("changeColorBtn").onclick = function() {
    document.getElementById("myHeading").style.color = "red";
};

// Move heading
document.getElementById("moveHeadingBtn").onclick = function() {
    let heading = document.getElementById("myHeading");
    heading.style.left = "50px";
};

// Change image source
document.getElementById("changeImageBtn").onclick = function() {
    document.getElementById("myImage").src = "https://via.placeholder.com/200/ff0000/ffffff?text=New+Image";
};

// Add text node
document.getElementById("addTextNodeBtn").onclick = function() {
    let newText = document.createTextNode("This is a dynamically added text.");
    document.getElementById("container").appendChild(newText);
};

// Delete first paragraph
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
    // Change button text
    $("#changeTextBtn").text("Click to Change Heading Text");

    // Set background image for container
    $("#container").css({
        "background-image": "url('https://via.placeholder.com/400x100')",
        "background-size": "cover"
    });

    // Access form data
    $("#myForm").submit(function(event) {
        event.preventDefault();
        let name = $("#nameInput").val();
        let age = $("#ageInput").val();
        alert(`Name: ${name}\nAge: ${age}`);
    });

    // Add attribute
    $("#myImage").attr("title", "This is a dynamically added title");
});
