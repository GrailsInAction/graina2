<div class="profilePic">
    <g:if test="${profile.photo}">
        <g:img controller="image" action="renderImage" id="${loginId}"/>
    </g:if>
    <p>Profile for <strong>${profile.fullName}</strong></p>
    <p>Bio: ${profile.bio}</p>
</div>
