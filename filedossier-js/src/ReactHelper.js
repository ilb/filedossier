import { useState, useEffect} from 'react';

export function useResource(api, command, params) {
    const [data, setData] = useState({value: null, error: null, loading: false});

    const fetch = async function () {
        console.log('fetch', api, params);
        setData({value: null, error: null, loading: true});
        try {
            //const json = await api.getDossier(params[0],params[1],params[2]);
            const json = await api[command](...params);
            console.log('json data', json);
            setData({value: json, error: null, loading: false});
        } catch (e) {
            console.log('error', e);
            setData({value: null, error: e.statusText, loading: false});
        }
    };

    useEffect(function () {
        console.log('useEffect');
        fetch();
    }, params);

    return [data, fetch];
}
