/**
 * This example is following frontend and backend separation.
 *
 * Before this .js is loaded, the html skeleton is created.
 *
 * This .js performs three steps:
 *      1. Get parameter from request URL so it know which id to look for
 *      2. Use jQuery to talk to backend API to get the json data.
 *      3. Populate the data to correct html elements.
 */


/**
 * Retrieve parameter from request URL, matching by parameter name
 * @param target String
 * @returns {*}
 */
function getParameterByName(target) {
    // Get request URL
    let url = window.location.href;
    // Encode target parameter name to url encoding
    target = target.replace(/[\[\]]/g, "\\$&");

    // Ues regular expression to find matched parameter value
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';

    // Return the decoded parameter value
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/**
 * Handles the data returned by the API, read the jsonObject and populate data into html elements
 * @param resultData jsonObject
 */

function handleResult(resultData) {

    console.log("handleResult: populating location info from resultData");

    // populate the star info h3
    // find the empty h3 body by id "star_info"
    let locationNameElement = jQuery("#location_name");

    // append two html <p> created to the h3 body, which will refresh the page
    locationNameElement.append("<p>City: " + resultData[0]["city"] + "</p>" +
        "<p>State: " + resultData[0]["state"] + "</p>"
        );

    let locationInfoElement = jQuery("#location_info");

    // append two html <p> created to the h3 body, which will refresh the page
    const lst_link = JSON.parse(localStorage.getItem("lstLink"));
    locationInfoElement.append(
        "<p>" +
        '<a href="' + lst_link + '">'
        + "Back to School List" +
        '</a>' +
        "</p>"+
        "<p>Zipcode: " + resultData[0]["zipcode"] + "</p>"+
        "<p>Living Cost Index: " + resultData[0]["LivingCostIndex"] +"</p>"+
        "<p>Safety Level: " + resultData[0]["safety_level"] + " out of 10"+"</p>"+
        "<p>Schools in This Location: " +"</p>"
    );

    console.log("handleResult: populating school table from resultData");


    // Populate the star table
    // Find the empty table body by id "movie_table_body"
    let schoolTableBodyElement = jQuery("#school_table_body");

    // Concatenate the html tags with resultData jsonObject to create table rows
    for (let i = 0; i < Math.min(10, resultData.length); i++) {
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
        rowHTML += "<th>" + resultData[i]["school_dis"] + "</th>";
        rowHTML += "</tr>";

        // Append the row created to the table body, which will refresh the page
        schoolTableBodyElement.append(rowHTML);
    }
}

/**
 * Once this .js is loaded, following scripts will be executed by the browser\
 */

// Get id from URL
let location_id = getParameterByName("id");

// Makes the HTTP GET request and registers on success callback function handleResult
jQuery.ajax({
    dataType: "json",  // Setting return data type
    method: "GET",// Setting request method
    url: "api/single-location?id=" + location_id, // Setting request url, which is mapped by StarsServlet in Stars.java
    success: (resultData) => handleResult(resultData) // Setting callback function to handle data returned successfully by the SingleStarServlet
});
