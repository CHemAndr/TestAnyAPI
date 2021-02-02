package home.api;

public class Jresponse {

    public Cterm[] suggestions;
    public String[] products;
    public Cpages[] pages;
    public boolean showSeeAllProducts;
    public boolean showSeeAllPages;

    public void Jresponse(Cterm[] suggestions, String[] products,
                          Cpages[] pages, boolean showSeeAllProducts, boolean showSeeAllPages ) {
        this.suggestions = suggestions;
        this.products = products;
        this.pages = pages;
        this.showSeeAllProducts = showSeeAllProducts;
        this.showSeeAllPages = showSeeAllPages;
    }

    public Cterm[] getSuggestions() {return this.suggestions; }
    public String[] getProducts() {return this.products;}
    public Cpages[] getPages() {return this.pages;}
    public boolean getShowSeeAllProducts() {return this.showSeeAllProducts;}
    public boolean getShowSeeAllPages() {return this.showSeeAllPages;}


    public static class Cterm {
        public String term;
        public void Cterm(String term){this.term = term;}
        public String getTerm() {return this.term;}

    }

    public static class Cpages {
        public String id;
        public double boost;
        public String title;
        public String url;
        public String content;

        public void Cpages(String id, double boost,String title,String url,String content){
            this.id = id;
            this.boost = boost;
            this.title = title;
            this.url = url;
            this.content = content;}

        public String getId() {return this.id;}
        public double getBoost() {return this.boost;}
        public String getTitle() {return  this.title;}
        public String getUrl() {return  this.url;}
        public String getContent() {return this.content;}

    }

}
