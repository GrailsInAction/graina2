<%@ page contentType="application/xml; charset=utf-8" %>
<posts>
  <g:each in="${posts}" var="post">
    <post>
      <content>${post.content}</content>
      <created><g:formatDate date="${post.created}" format="yyyy-MM-dd"/></created>
      <user>
        <userId>${post.user.userId}</userId>
        <fullName>${post.user.profile.fullName}</fullName>
      </user>
    </post>
  </g:each>
</posts>
