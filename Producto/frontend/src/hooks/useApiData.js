import { useState, useEffect } from 'react';

export default function useApiData(endpoint) {
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch(`http://localhost:3001/${endpoint}`)
            .then(res => res.json())
            .then(setData)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, [endpoint]);

    return { data, loading };
}
