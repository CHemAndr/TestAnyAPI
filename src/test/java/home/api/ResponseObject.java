package home.api;

public class ResponseObject {

    public Term[] suggestions;
    public String[] products;
    public Pages[] pages;
    public boolean showSeeAllProducts;
    public boolean showSeeAllPages;

    public ResponseObject(Term[] suggestions, String[] products,
                               Pages[] pages, boolean showSeeAllProducts, boolean showSeeAllPages ) {
        this.suggestions = suggestions;
        this.products = products;
        this.pages = pages;
        this.showSeeAllProducts = showSeeAllProducts;
        this.showSeeAllPages = showSeeAllPages;
    }

    public Term[] getSuggestions() {
        return this.suggestions;
    }
    public String[] getProducts() {
        return this.products;
    }
    public Pages[] getPages() {
        return this.pages;
    }
    public boolean getShowSeeAllProducts() {
        return this.showSeeAllProducts;
    }
    public boolean getShowSeeAllPages() {
        return this.showSeeAllPages;
    }


    public static class Term {
        public String term;
        public Term(String term){
            this.term = term;
        }
        public String getTerm() {
            return this.term;
        }

    }

    public static class Pages {
        public String id;
        public double boost;
        public String title;
        public String url;
        public String content;

        public Pages(String id, double boost,String title,String url,String content){
            this.id = id;
            this.boost = boost;
            this.title = title;
            this.url = url;
            this.content = content;}

        public String getId() {
            return this.id;
        }
        public double getBoost() {
            return this.boost;
        }
        public String getTitle() {
            return  this.title;
        }
        public String getUrl() {
            return  this.url;
        }
        public String getContent() {
            return this.content;
        }
    }
}
