import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'college-info',
        data: { pageTitle: 'automaticTimeTableGeneratorApp.collegeInfo.home.title' },
        loadChildren: () => import('./college-info/college-info.module').then(m => m.CollegeInfoModule),
      },
      {
        path: 'course',
        data: { pageTitle: 'automaticTimeTableGeneratorApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'semister',
        data: { pageTitle: 'automaticTimeTableGeneratorApp.semister.home.title' },
        loadChildren: () => import('./semister/semister.module').then(m => m.SemisterModule),
      },
      {
        path: 'subject',
        data: { pageTitle: 'automaticTimeTableGeneratorApp.subject.home.title' },
        loadChildren: () => import('./subject/subject.module').then(m => m.SubjectModule),
      },
      {
        path: 'lecturer',
        data: { pageTitle: 'automaticTimeTableGeneratorApp.lecturer.home.title' },
        loadChildren: () => import('./lecturer/lecturer.module').then(m => m.LecturerModule),
      },
      {
        path: 'generate-time-table',
        data: { pageTitle: 'automaticTimeTableGeneratorApp.generateTimeTable.home.title' },
        loadChildren: () => import('./generate-time-table/generate-time-table.module').then(m => m.GenerateTimeTableModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
