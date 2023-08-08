let checkout_form = $("#checkout_form");


function calculateMatch(resultArray) {
    let serializedData = checkout_form.serialize();
    const formData = new URLSearchParams(serializedData);
    const SAT_Param = formData.get("SAT")
    const cost_Param = formData.get("cost");
    const genre_Param = formData.get("genre");

    const SAT = SAT_Param.substring(SAT_Param.indexOf('=') + 1);
    const cost = cost_Param.substring(cost_Param.indexOf('=') + 1);
    const genre = genre_Param.substring(genre_Param.indexOf('=') + 1);

    let student_info = [SAT, cost, genre]

    localStorage.setItem("student_data", JSON.stringify(student_info));
    localStorage.setItem("school_data", JSON.stringify(resultArray));


    for (let i = 0; i < resultArray.length; i++) {
        let innerArray = JSON.parse(resultArray[i]);
        console.log(innerArray);
    }
    console.log(resultArray);
    console.log(SAT);
    console.log(cost);
    console.log(genre);
    window.location.replace('check.html');
}
function submitCheckoutForm(formSubmitEvent) {
    console.log("submit user checkout form");
    /**
     * When users click the submit button, the browser will not direct
     * users to the url defined in HTML form. Instead, it will call this
     * event handler when the event is triggered.
     */
    formSubmitEvent.preventDefault();

    $.ajax("api/cart", {
        method: "GET",
        // Serialize the login form to the data sent by POST request
        success: resultDataString => {
            let resultDataJson = JSON.parse(resultDataString);
            calculateMatch(resultDataJson["previousItems"]);
        }
    });
}
checkout_form.submit(submitCheckoutForm);