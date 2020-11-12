export interface Course {
  id: number;
  name: string;
}

export interface Post {
  id: number;
  name: string;
  content: string;
  chores: Chore[];
}

export interface Chore {
  key: number;
  id: number | null;
  name?: string;
  courseName?: string;
  courseId?: number;
  postName?: string;
  postId?: number;
}

export interface User {
  id: number;
  email?: string;
}
