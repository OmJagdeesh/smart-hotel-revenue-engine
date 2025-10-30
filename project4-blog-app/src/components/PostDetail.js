import React from "react";
import { useParams, Link } from "react-router-dom";
import posts from "../data/posts";

function PostDetail() {
  const { id } = useParams();
  const post = posts.find(p => p.id === parseInt(id));

  if (!post) return <h2>Post not found</h2>;

  return (
    <div className="container">
      <h1 className="title">{post.title}</h1>
      <p className="content">{post.content}</p>
      <Link className="btn" to="/">← Back to Home</Link>
    </div>
  );
}

export default PostDetail;
