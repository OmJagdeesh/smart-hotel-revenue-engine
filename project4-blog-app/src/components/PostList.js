import React from "react";
import { Link } from "react-router-dom";
import posts from "../data/posts";

function PostList() {
  return (
    <div className="container">
      <h1 className="title">My Blog</h1>
      <div className="post-list">
        {posts.map(post => (
          <div className="post-card" key={post.id}>
            <h2>{post.title}</h2>
            <p>{post.content.slice(0, 60)}...</p>
            <Link className="btn" to={`/post/${post.id}`}>
              Read More
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
}

export default PostList;
