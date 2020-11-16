import restClient from "./restClient";
import { NewPostRequest, PostResponse } from "@/client/types";

export async function addNewPost(
  courseId: number,
  newCourseRequest: NewPostRequest
): Promise<void> {
  return await restClient.post(
    "/courses/" + courseId + "/posts",
    newCourseRequest
  );
}

export async function updateExistingPost(
  courseId: number,
  newCourseRequest: NewPostRequest
): Promise<void> {
  return await restClient.put(
    "/courses/" + courseId + "/posts",
    newCourseRequest
  );
}

export async function getPostContent(postId: number): Promise<PostResponse> {
  return await restClient.get("/posts/" + postId);
}

export async function getPostsForCourse(
  courseId: number
): Promise<PostResponse[]> {
  return await restClient.get("/courses/" + courseId + "/posts");
}
