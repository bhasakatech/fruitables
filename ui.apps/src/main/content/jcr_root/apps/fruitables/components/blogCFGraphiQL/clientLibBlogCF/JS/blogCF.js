document.addEventListener("DOMContentLoaded",function(){
const container = document.getElementById("blog-container");
const dropdown = document.getElementById("category-dropdown");
  function fetchBlogs(category){
    container.innerHTML="";
    fetch(`/graphql/execute.json/fruitables/get-blog-category;category=${category}`)
    .then(res => res.json())
    .then(data => {
      const blogs = data.data.blogList.items;
      const containerql = document.getElementById("blog-container");

      blogs.forEach(blog => {
        const datql = document.createElement("div");
        datql.className = "blog-card";

        datql.innerHTML = `
          <h2>${blog.title}</h2>
          <p><span>Category:</span> ${blog.category}</p>
          <p><span>Date:</span> ${blog.publishDate}</p>
          <p>${blog.description.plaintext}</p>
        `;

        containerql.appendChild(datql);
      });
    })
    .catch(err => console.error(err));
  }
  
dropdown.addEventListener("change",(e)=>{
    const selectedCategory = e.target.value;
    fetchBlogs(selectedCategory);
});
});
