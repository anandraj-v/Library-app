import { useEffect, useState } from "react";
import { useSearchParams } from "react-router-dom";
import BookModel from "../../models/BookModel";
import ReviewModel from "../../models/ReviewModel";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { StarsReview } from "../Utils/StarsReview";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";
import { LatestReviews } from "./LatestReviews";

export const BookCheckoutPage = () => {

    const [book, setBook] = useState<BookModel>();
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);

    //Reviews state
    const [reviews, setReviews] = useState<ReviewModel[]>([]);
    const [totalStars, setTotalStars] = useState(0);
    const [isLoadingReviews, setIsLoadingReviews] = useState(true);

    const bookId = (window.location.pathname).split('/')[2];

    useEffect(() => {
        const fetchBooks = async () => {
            const baseUrl: string = `http://localhost:8081/api/books/${bookId}`;

            const token = localStorage.getItem("jwtToken");
            const response = await fetch(baseUrl, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (!response.ok) {
                throw new Error('something went wrong!');
            }

            const responseJson = await response.json();



            const loadedBooks: BookModel = {
                id: responseJson.id,
                title: responseJson.title,
                author: responseJson.author,
                description: responseJson.description,
                copies: responseJson.copies,
                copiesAvailable: responseJson.copiesAvailable,
                category: responseJson.category,
                img: responseJson.img

            };
            setBook(loadedBooks);

            setIsLoading(false);
        };
        fetchBooks().catch((error: any) => {
            setIsLoading(false);
            setHttpError(error.message);
        })
    }, []);

    useEffect(() => {
        const fetchBookReviews = async () => {
            const reviewUrl: string = `http://localhost:8081/api/reviews/search/findByBookId?bookId=${bookId}`;

            const token = localStorage.getItem("jwtToken");
            
            const responseReviews = await fetch(reviewUrl, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            });

            if (!responseReviews.ok) {
                throw new Error('something went wrong!');
            }

            const responseJson = await responseReviews.json();

            const responseData = responseJson._embedded.reviews;

            const loadedReviews: ReviewModel[] = [];

            let weightedStarReviews: number = 0;

            for (const review of responseData) {
                loadedReviews.push({
                    id: review.id,
                    userEmail: review.userEmail,
                    date: review.date,
                    rating: review.rating,
                    book_Id: review.bookId, // Maps backend 'bookId' to frontend 'book_Id'
                    reviewDescription: review.reviewDescription
                });
                weightedStarReviews = weightedStarReviews + review.rating;
            }
            if (loadedReviews){
                const round = (Math.round((weightedStarReviews / loadedReviews.length) * 2) / 2).toFixed(1);
                setTotalStars(Number(round));
            }
            setReviews(loadedReviews);
            setIsLoadingReviews(false);
        }
        fetchBookReviews().catch((error: any) => {
            setIsLoadingReviews(false);
            setHttpError(error.message);
        });
    }, []);
    if (isLoading || isLoadingReviews) {
        return (
            <SpinnerLoading />
        );
    }
    if (httpError) {
        return (
            <div className="container mt-5">
                <p>{httpError}</p>
            </div>
        );
    }
    return (
        <div>
            <div className=' container d-none d-lg-block'>
                <div className='row mt-5'>
                    <div className='col-sm-2 col-md-2'>
                        {
                            book?.img ?
                                <img src={book?.img} width='226' height='349' alt='book' />
                                :
                                <img src={require('./../../Images/BooksImages/book-luv2code-1000.png')} width='226'
                                    height='349' alt='book' />
                        }
                    </div>
                    <div className='col-4 col-md-4 container'>
                        <div className='ml-2'>
                            <h2>{book?.title}</h2>
                            <h5 className='text-primary'>{book?.author}</h5>
                            <p className='lead'>{book?.description}</p>
                            <StarsReview rating={totalStars} size={32} />
                        </div>
                    </div>
                    <CheckoutAndReviewBox book={book} mobile={false} />
                </div>
                <hr />
                <LatestReviews reviews={reviews} bookId={book?.id} mobile={false}/>
            </div>
            <div className='container d-lg-none mt-5'>
                <div className='d-flex justify-content-centre align-items-centre'>
                    {
                        book?.img ?
                            <img src={book?.img} width='226' height='349' alt='book' />
                            :
                            <img src={require('./../../Images/BooksImages/book-luv2code-1000.png')} width='226'
                                height='349' alt='book' />
                    }
                </div>
                <div className='mt-4'>
                    <div className='ml-2'>
                        <h2>{book?.title}</h2>
                        <h5 className='text-primary'>{book?.author}</h5>
                        <p className='lead'>{book?.description}</p>
                        <StarsReview rating={totalStars} size={32} />
                    </div>
                </div>
                <CheckoutAndReviewBox book={book} mobile={true} />
                <hr />
                <LatestReviews reviews={reviews} bookId={book?.id} mobile={true} />
            </div>
        </div>
    );
}