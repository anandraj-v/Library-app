import { useEffect, useState } from "react";

export const DataFetcher = (props: { url: any; render: any }) => {
    const { url, render } = props;
    const [data, setData] = useState<any>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);

    useEffect(() => {
        fetch(url)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then((data) => {
                setData(data);
                setIsLoading(false);
            })
            .catch((error) => {
                setHttpError(error.message);
                setIsLoading(false);
            });
    }, [url]);
    return render({ data, isLoading, httpError });
}