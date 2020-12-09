 function promptDelete() {
    document.getElementById('delete').onclick = function () {
        return prompt("Are you sure?")
    };
}