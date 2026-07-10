class ReviewModel{

    id: number;
    userEmail: string;
    date: string;
    rating: number;
    book_Id: number;
    reviewDescription?: string;
    constructor(id: number,
    userEmail: string,
    date: string,
    rating: number,
    book_Id: number,
    reviewDescription: string){


        this.id = id;
        this.userEmail = userEmail;
        this.date = date;
        this.rating = rating;
        this.book_Id = book_Id;
        this.reviewDescription = reviewDescription;
        
    }
}
export default ReviewModel;