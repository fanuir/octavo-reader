document.addEventListener("DOMContentLoaded", function(){
    var spans = document.getElementsByTagName("span");
    for(var i = 0; i < spans.length; i++){
        var span = spans[i];
        if(span.hasAttribute("title")){
            span.addEventListener("click", function(event){
                event.preventDefault();
                var words = this.getAttribute("title");
                Android.showToast(words);
            });
            //console.log(span.getAttribute("title"));
        }
    }
    var spans = document.getElementsByTagName("a");
    for(var i = 0; i < spans.length; i++){
        var span = spans[i];
        if(span.hasAttribute("title")){
            span.addEventListener("click", function(event){
                event.preventDefault();
                var words = this.getAttribute("title");
                Android.showToast(words);
            });
            //console.log(span.getAttribute("title"));
        }
    }
});