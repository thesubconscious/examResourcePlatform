interface Course {
    course_id: string
    name: string
    introduction: string
    img_path: string
    teacher: Teacher;
}

interface Teacher {
    name: string
    teacher_id: string
}