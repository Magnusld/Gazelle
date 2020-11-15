import { User } from "@/types";

export interface LogInRequest {
  email: string;
  password: string;
}

export interface SignUpRequest {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
}

export interface LogInResponse {
  user: User;
  token: string;
}

export interface PostResponse {
  id: number;
  title: string;
  startDate: Date;
  endDate: Date;
  choresDone: number;
  choresFocused: number;
  choresCount: number;
}

export interface PostRequest {

}

export interface CourseRequest {

}

export interface CourseResponse {
  id: number;
  name: string;
  isOwner: boolean;
  isFollower: boolean;
  currentPost: PostResponse;
  nextPost: PostResponse;
}
