export interface Course {
    id: number;
    name: string;
}

export interface User {
    id: number;
    username: string;
}

export interface CourseRoleKey {
    userId: number;
    courseId: number;
}

export interface CourseRole {
    id: CourseRoleKey;
    user: User;
    course: Course;
    courseRoleType: CourseRoleType;
}

enum CourseRoleType {
    FOLLOWER,
    OWNER
}