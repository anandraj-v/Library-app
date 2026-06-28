import { useEffect, useState } from "react";
import { DataFetcher } from "../Utils/DataFetcher";
import BookModel from "../../models/BookModel";
import { SpinnerLoading } from "../Utils/SpinnerLoading";
import { SearchBook } from "../SearchBooksPage/SearchBook";
import { Pagination } from "../Utils/Pagination";

export const DataFetchRuf = () => {
    const [currentPage, setCurrentPage] = useState(1);
    const [booksPerPage] = useState(5);

    const baseUrl: string = "http://localhost:8081/api/books";
    const url: string = `${baseUrl}?page=${currentPage - 1}&size=${booksPerPage}`;

    useEffect(() => {
        window.scrollTo(0, 0);
    }, [currentPage]);

    const dataFetch = ({ data, isLoading, httpError }: { data: any; isLoading: boolean; httpError: string | null }) => {
        if (isLoading) {
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

        const responseData = data?._embedded?.books ?? [];
        const totalAmountOfBooks = data?.page?.totalElements ?? 0;
        const totalPages = data?.page?.totalPages ?? 0;

        const loadedBooks: BookModel[] = responseData.map((item: any) => ({
            id: item.id,
            title: item.title,
            author: item.author,
            description: item.description,
            copies: item.copies,
            copiesAvailable: item.copiesAvailable,
            category: item.category,
            img: item.img
        }));

        const indexOfLastBook: number = currentPage * booksPerPage;
        const indexOfFirstBook: number = indexOfLastBook - booksPerPage;
        const lastItem = booksPerPage * currentPage <= totalAmountOfBooks ? indexOfLastBook : totalAmountOfBooks;
        const paginate = (pageNumber: number) => setCurrentPage(pageNumber);

        return (
            <div>
                <div className='container'>
                    <div className='row mt-5'>
                        <div className='col-6'>
                            <div className='d-flex'>
                                <input className='form-control me-2' type='search' placeholder='Search' aria-label='Search' />
                                <button className='btn btn-outline-success'>Search</button>
                            </div>
                        </div>
                        <div className='col-4'>
                            <div className='dropdown'>
                                <button className='btn btn-secondary dropdown-toggle' type='button' id='dropdownMenuButton1'
                                    data-bs-toggle='dropdown' aria-expanded='false'>
                                    Category
                                </button>
                                <ul className='dropdown-menu' aria-labelledby='dropdownMenuButton1'>
                                    <li><a className='dropdown-item' href='#'>All</a></li>
                                    <li><a className='dropdown-item' href='#'>Front End</a></li>
                                    <li><a className='dropdown-item' href='#'>Back End</a></li>
                                    <li><a className='dropdown-item' href='#'>Data</a></li>
                                    <li><a className='dropdown-item' href='#'>DevOps</a></li>
                                </ul>
                            </div>
                        </div>
                        <div className='mt-3'>
                            <h5>Number of Results: ({totalAmountOfBooks})</h5>
                        </div>
                        <p>
                            {indexOfFirstBook + 1} to {lastItem} of {totalAmountOfBooks} items:
                        </p>
                        {loadedBooks.map((book) => (
                            <SearchBook book={book} key={book.id} />
                        ))}
                        {totalPages > 1 && (
                            <Pagination currentPage={currentPage} totalPages={totalPages} paginate={paginate} />
                        )}
                    </div>
                </div>
            </div>
        );

    }

    return (
        <DataFetcher
            url={url} render={dataFetch}
        />
    );
}




