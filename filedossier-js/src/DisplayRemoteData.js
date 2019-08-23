import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';

import { useFetch } from 'react-hooks-fetch';

const Err = ({ error }) => <span>Error:{error.message}</span>;

const DisplayRemoteDataInner = () => {
  const url = 'https://jsonplaceholder.typicode.com/posts/1';
  const { error, data } = useFetch(url);
  if (error) return <Err error={error} />;
  if (!data) return null;
  return <span>RemoteData:{data.title}</span>;
};

const DisplayRemoteData = () => (
  <Suspense fallback={<span>Loading...</span>}>
    <DisplayRemoteDataInner />
  </Suspense>
);

export default DisplayRemoteData;
