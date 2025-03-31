interface Course {
    course_id: number
    name: string
    introduction: string
    img_path: string
    teacher: Teacher;
}

interface Teacher {
    name: string
    teacher_id: number
}