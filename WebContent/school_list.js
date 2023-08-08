/**
 * This .js performs two steps:
 *      1. Use jQuery to talk to backend API to get the json data.
 *      2. Populate the data to correct html elements.
 */


/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */
let sort_form = $("#sort_form");
let page_form = $("#page_form");

function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    // System.out.println(target);
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function getInfo(school_name, schoolID) {
    // called by button onclick "Add to List" and sends an ajax request to SingleSchoolServlet to retrieve needed info
    console.log("clicked " + school_name)
    $.ajax("api/single-school", {
        method: "GET",
        data: {id: schoolID, name: school_name},
        success: resultData => {
            console.log(resultData[0]["school_name"] + " " + resultData[0]["genre"] + " " + resultData[0]["state"]);
            window.alert(resultData[0]["school_name"] + " added to wishlist!");
            addToCart(resultData);
        }
    });
}

function addToCart(DataJsonArray) {
    const score = window.prompt("Give a score on how much you love this school (from 1-5):");
    // called by a successful call to getInfo; sends an ajax POST request to ShoppingCartServlet to add new school to shopping cart
    console.log("enters add to cart");
    console.log(DataJsonArray[0]["net_cost"] + " " + DataJsonArray[0]["lower_SAT"]);
    $.ajax("api/cart", {
        method: "POST",
        data:
            {school_name: DataJsonArray[0]["school_name"], genre: DataJsonArray[0]["genre"], state: DataJsonArray[0]["state"],
                city: DataJsonArray[0]["city"], safety_level :DataJsonArray[0]["safety_level"],
                school_id: DataJsonArray[0]["school_id"], location_id: DataJsonArray[0]["location_id"], lower_SAT: DataJsonArray[0]["lower_SAT"],
                upper_SAT: DataJsonArray[0]["upper_SAT"], net_cost: DataJsonArray[0]["net_cost"], preference: score}
    });
}


function handleSchoolResult(resultData) {
    console.log("handleSchoolResult: populating School table from resultData");

    let schoolTableBodyElement = jQuery("#school_table_body");

    resultPerpage = resultData[0]["pagenum"]
    whichpage = resultData[0]["whichpage"]

    console.log("resultPerpage: " + resultPerpage);
    console.log("whichpage: " + whichpage);
    console.log("resultData length: " + resultData.length);
    console.log(Math.min( resultPerpage, resultData.length)+1);


    // Iterate through resultData, no more than 10 entries
    for (let i = 1; i < Math.min( resultPerpage, resultData.length-1)+1; i++) {
        // Concatenate the html tags with resultData jsonObject
        console.log(resultData[i]["school_name"]);
        let rowHTML = "";
        rowHTML += "<tr>";
        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-school.html?id=' + resultData[i]['school_id'] + '">'
            + resultData[i]["school_name"] +     // display school_name for the link text
            '</a>' +
            "</th>";

        rowHTML += "<th>" + resultData[i]["school_rating"] + "</th>";

        rowHTML +=
            "<th>" +
            // Add a link to single-school.html with id passed with GET url parameter
            '<a href="single-location.html?id=' + resultData[i]['location_id']+ '">'
            + resultData[i]["school_city"] +", "+ resultData[i]["school_state"] +    // display school_name for the link text
            '</a>' +
            "</th>";
        rowHTML += "<th>Website: <a href='" + resultData[i]["link_to_website"] + "'>" + resultData[i]["link_to_website"] + "</a></th>"
        rowHTML += "<th>" + resultData[i]["safety"] + "</th>";
        rowHTML += "<th>" + resultData[i]["telephone"] + "</th>";
        // Adds a "Add to List" button to each row of record in school-list
        rowHTML += `<th><button onclick="getInfo(`;
        rowHTML += "'" + resultData[i]["school_name"] + "', '" + resultData[i]["school_id"] + "'"
        rowHTML += ')">Add to list</button></th>';
        rowHTML += "</tr>";
        console.log(rowHTML);

        // Append the row created to the table body, which will refresh the page
        schoolTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser
 */
console.log("1");

let schoolName = getParameterByName("school");
let locationName = getParameterByName("location");
let otherName = getParameterByName("other");
let orderName = getParameterByName("order");
let genreName = getParameterByName("genre");
let pagenum = getParameterByName("pagenum");
console.log("2");

let whichpage = getParameterByName("whichpage");
console.log("3");
console.log(whichpage);


// Makes the HTTP GET request and registers on success callback function handleStarResult
jQuery.ajax({
    dataType: "json", // Setting return data type
    method: "GET", // Setting request method
    url: "api/school_list?school=" + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+ pagenum+ "&whichpage="+ whichpage,// Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleSchoolResult(resultData) // Setting callback function to handle data returned successfully by the StarsServlet
});

function submitSortForm(event) {
    event.preventDefault();
    const sortValue = $('#sort').val();
    localStorage.setItem("lstLink",JSON.stringify('school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ sortValue + "&genre="+ genreName+ "&pagenum="+ pagenum+ "&whichpage="+ whichpage));
    window.location.replace('school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ sortValue + "&genre="+ genreName+ "&pagenum="+ pagenum+ "&whichpage="+ whichpage);
}

function submitPageForm(event) {
    event.preventDefault();
    const pageValue = $('#page').val();
    window.location.replace( 'school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+pageValue+ "&whichpage="+ whichpage);// Setting request url, which is mapped by StarsServlet in Stars.java
}

sort_form.submit(submitSortForm);
page_form.submit(submitPageForm);


const previousBtn = document.querySelector("#previousBtn");
const nextBtn = document.querySelector("#nextBtn");
previousBtn.addEventListener("click", function(event) {
    // Handle previous button click
    console.log("Previous button clicked");
    if (parseInt(whichpage)-1>0){
        whichpage = (parseInt(whichpage)-1).toString();
    }
    else{
        whichpage = "0";
    }
    window.location.replace( 'school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+pagenum+ "&whichpage="+ whichpage);// Setting request url, which is mapped by StarsServlet in Stars.java
});
nextBtn.addEventListener("click", function(event) {
    // Handle next button click
    console.log("Next button clicked");
    whichpage = (parseInt(whichpage)+1).toString();
    window.location.replace( 'school_list.html?school=' + schoolName+"&location=" + locationName+"&other="+ otherName+"&order="+ orderName + "&genre="+ genreName+ "&pagenum="+pagenum+ "&whichpage="+ whichpage);// Setting request url, which is mapped by StarsServlet in Stars.java
});