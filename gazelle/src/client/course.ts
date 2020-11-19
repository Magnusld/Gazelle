import restClient from "./restClient";
import {
  CourseContentResponse,
  CourseResponse,
  NewCourseRequest
} from "@/client/types";

export async function addNewCourse(
  newCourseRequest: NewCourseRequest
): Promise<void> {
  return await restClient.post("/courses", newCourseRequest);
}

export async function deleteCourse(id: number): Promise<void> {
  return await restClient.delete("/courses/" + id);
}

export async function findCourseById(
  id: number
): Promise<CourseContentResponse> {
  return await restClient.get("/courses/" + id);
}

export async function findAllCourses(): Promise<CourseResponse[]> {
  return await restClient.get("/courses");
}

export async function getFollowedCourses(
  userId: number
): Promise<CourseResponse[]> {
  return await restClient.get("/users/" + userId + "/followedCourses");
}

export async function getCourseFollowers(courseId: number): Promise<void> {
  return await restClient.get("/courses/" + courseId + "/followers");
}

export async function addCourseFollower(
  courseId: number,
  userId: number
): Promise<void> {
  return await restClient.post("/users/" + userId + "/followedCourses", {
    value: courseId
  });
}

export async function removeCourseFollower(
  courseId: number,
  userId: number
): Promise<void> {
  return await restClient.delete(
    "/users/" + userId + "/followedCourses/" + courseId
  );
}

export async function getOwnedCourses(
  userId: number
): Promise<CourseResponse[]> {
  return await restClient.get("/users/" + userId + "/ownedCourses");
}

export async function getCourseOwners(courseId: number): Promise<void> {
  return await restClient.get("/courses/" + courseId + "/owners");
}

export async function addCourseOwner(
  courseId: number,
  userId: number
): Promise<void> {
  return await restClient.post("/users/" + userId + "/ownedCourses", {
    value: courseId
  });
}

export async function removeCourseOwner(
  courseId: number,
  userId: number
): Promise<void> {
  return await restClient.delete(
    "/users/" + userId + "/ownedCourses/" + courseId
  );
}
