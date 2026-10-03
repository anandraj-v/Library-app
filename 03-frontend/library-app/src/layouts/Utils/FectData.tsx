import { JSX, useEffect, useState } from "react"
import { Url } from "url";

export const FetchData = (
    { url, login, formData, render }:
        {
            url: RequestInfo | URL; login?: boolean; formData?: any; render:
                (args: { data: any; loading: boolean; error: any; token: boolean }) =>JSX.Element | null
        }) => {
    const [data, setData] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [token, setToken] = useState(false);
    const [localFormData, setLocalFormData] = useState({})

    useEffect(() => {
        setLocalFormData(formData);
        const fetchData = async () => {

            const options: any = login ? {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(localFormData),
            } : {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    "Content-Type": "application/json"
                }
            };

            fetch(url, options).then((response) => {
                if (!response.ok) {
                    throw new Error('something went wrong!');
                }
                return response.json();
            }).then((data) => {
                setToken(data.token);
                setData(data);
                setLoading(false);
            }).catch((error) => {
                setError(error);
                setLoading(false);
            });
        }
        //fetchData();
    }, [url, formData]);
    return render({ data, loading, error, token });
}