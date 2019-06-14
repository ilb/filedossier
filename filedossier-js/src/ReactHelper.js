import { useState } from 'react';

export function useResource(source) {
    const [data, setData] = useState({value: null, error: null, loading: false});
    const fetch = async function () {
        setData({value: null, error: null, loading: true});
        try {
            const json = await source();
            console.log('json data', json);
            setData({value: json, error: null, loading: false});
        } catch (e) {
            console.log('error', e);
            setData({value: null, error: e.statusText, loading: false});
        }
    };

    return [data, fetch];
}
