import restClient from "./restClient";
import {
  NewPostRequest,
  PostContentResponse,
  PostResponse
} from "@/client/types";

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
  postId: number,
  newCourseRequest: NewPostRequest
): Promise<void> {
  return await restClient.put("/posts/" + postId, newCourseRequest);
}

export async function deletePost(postId: number): Promise<void> {
  return await restClient.delete("/posts/" + postId);
}

export async function getPostContent(
  postId: number
): Promise<PostContentResponse> {
  return await restClient.get("/posts/" + postId);
}

export async function getPostsForCourse(
  courseId: number
): Promise<PostResponse[]> {
  return await restClient.get("/courses/" + courseId + "/posts");
}
