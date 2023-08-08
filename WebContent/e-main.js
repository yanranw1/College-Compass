let search_form = $("#search_form");
let browse_form2 = $("#browse_form2");
let browse_form1 = $("#browse_form1");
const select = document.getElementById('browse_way1');

const options = ['Public University', 'Private University', 'Liberal Arts College', 'Technical College', 'Trade School', 'Alchemy School', 'Arts School', 'Online', 'Institution', 'Coding Bootcamp', 'Reading Club', 'Writers College', 'Training Facility', 'Cult', 'Military', 'Waldorf', 'Law School', 'Medical School', 'Business School', 'Community College', 'Vocational School', 'Music College', 'Mime School'];

for (let i = 0; i < options.length; i++) {
    const option = document.createElement('option');
    option.value = options[i];
    option.textContent = options[i];
    select.appendChild(option);
}

/**
 * Handle the data returned by LoginServlet
 * @param resultDataString jsonObject
 */
function handleSearchResult(resultDataString) {
    let resultDataJson = JSON.parse(JSON.stringify(resultDataString));

    console.log("handle search response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    // If search succeeds, it will redirect the user to index.html
    if (resultDataJson["status"] === "success") {
        localStorage.setItem("lstLink",JSON.stringify('school_list.html?school=' + resultDataJson['school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre']+ "&pagenum="+"20"+ "&whichpage=0" + "&groupby=school" + "&distinct=true"));
        window.location.replace('school_list.html?school=' + resultDataJson['school'] + "&location=" + resultDataJson['location'] + "&other=" + resultDataJson['other'] + "&order=" + resultDataJson['order'] + "&genre=" + resultDataJson['genre']+ "&pagenum="+"20"+ "&whichpage=0" + "&groupby=school" + "&distinct=true");
    } else {
        // If search fails, the web page will display
        // error messages on <div> with id "search_error_message"
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#search_error_message").text(resultDataJson["message"]);
    }
}

/**
 * Submit the form content with POST method
 * @param formSubmitEvent
 */
function submitSearchForm(formSubmitEvent) {
    console.log("submit search form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax(
        "api/search", {
            method: "POST",
            // Serialize the search form to the data sent by POST request
            data: search_form.serialize(),
            success: handleSearchResult
        }
    );
}
function submitBrowseForm(event) {
    event.preventDefault();

    // Get the selected value from the dropdown menu
    const browseWayValue2 = $('#browse_way2').val();
    const browseWayValue1 = $('#browse_way1').val();



    // Construct the search query using the form data and the dropdown value
    const searchQuery = {
        browse_way2: browseWayValue2, browse_way1: browseWayValue1
        // Add other form fields here
    };
    // Send the search query to the server using AJAX
    $.ajax({
        type: 'POST',
        url: 'api/search',
        data: searchQuery,
        success: handleSearchResult,
        error: function(jqXHR, textStatus, errorThrown) {
            // Handle errors
            console.log("Browse request failed: " + textStatus + ", " + errorThrown);
        }
    });
}

// Bind the submit action of the form to a handler function
search_form.submit(submitSearchForm);
browse_form1.submit(submitBrowseForm);
browse_form2.submit(submitBrowseForm);
