<%@ page contentType="application/xml; charset=utf-8" %>
<post>
  <content>${post.content}</content>
  <created><g:formatDate date="${post.created}" format="yyyy-MM-dd"/></created>
  <user>
    <userId>${post.user.userId}</userId>
    <fullName>${post.user.profile.fullName}</fullName>
  </user>
</post>
