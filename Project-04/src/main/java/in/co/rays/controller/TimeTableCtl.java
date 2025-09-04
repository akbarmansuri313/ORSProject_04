package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.TimetableBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.CourseModel;
import in.co.rays.model.SubjectModel;
import in.co.rays.model.TimetableModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * TimeTableCtl Servlet acts as a Controller to handle Timetable operations such
 * as add, update, preload, and validation. It interacts with
 * {@link TimetableModel}, {@link SubjectModel}, and {@link CourseModel}.
 * 
 * @author Akbar
 * @version 1.0
 * @since 2025
 */
@WebServlet(name = "/TimeTableCtl", urlPatterns = { "/ctl/TimeTableCtl" })
public class TimeTableCtl extends BaseClt {

	private static final Logger log = Logger.getLogger(TimeTableCtl.class);

	/**
	 * Preloads the list of Subjects and Courses and sets them in the request scope.
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("TimeTableCtl preload started");
		SubjectModel subjectModel = new SubjectModel();
		CourseModel courseModel = new CourseModel();

		try {
			List subjectList = subjectModel.list();
			request.setAttribute("subjectList", subjectList);

			List courseList = courseModel.list();
			request.setAttribute("courseList", courseList);

		} catch (ApplicationException e) {
			log.error("ApplicationException in preload: ", e);
		}
		log.debug("TimeTableCtl preload ended");
	}

	/**
	 * Validates the request parameters for timetable creation or update.
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.debug("TimeTableCtl validate started");
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("semester"))) {
			request.setAttribute("semester", PropertyReader.getValue("error.require", "Semester"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.require", "Date of Exam"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("examDate"))) {
			request.setAttribute("examDate", PropertyReader.getValue("error.date", "Date of Exam"));
			pass = false;
		} else if (DataValidator.isSunday(request.getParameter("examDate"))) {
			request.setAttribute("examDate", "Exam should not be on Sunday");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("examTime"))) {
			request.setAttribute("examTime", PropertyReader.getValue("error.require", "Exam Time"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("courseId"))) {
			request.setAttribute("courseId", PropertyReader.getValue("error.require", "Course Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("subjectId"))) {
			request.setAttribute("subjectId", PropertyReader.getValue("error.require", "Subject Name"));
			pass = false;
		}

		log.debug("TimeTableCtl validate ended with pass=" + pass);
		return pass;
	}

	/**
	 * Populates a TimetableBean object from request parameters.
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("TimeTableCtl populateBean started");
		TimetableBean bean = new TimetableBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setSemester(DataUtility.getString(request.getParameter("semester")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));
		bean.setExamTime(DataUtility.getString(request.getParameter("examTime")));
		bean.setExamDate(DataUtility.getDate(request.getParameter("examDate")));
		bean.setCourseId(DataUtility.getLong(request.getParameter("courseId")));
		bean.setSubjectId(DataUtility.getLong(request.getParameter("subjectId")));

		populateDTO(bean, request);

		log.debug("TimeTableCtl populateBean ended with bean: ");
		return bean;
	}

	/**
	 * Handles HTTP GET request.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimeTableCtl doGet started");

		long id = DataUtility.getLong(request.getParameter("id"));
		TimetableModel model = new TimetableModel();

		if (id > 0) {
			try {
				TimetableBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);

			} catch (ApplicationException e) {
				log.error("ApplicationException in doGet: ", e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
		log.debug("TimeTableCtl doGet ended");
	}

	/**
	 * Handles HTTP POST request.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("TimeTableCtl doPost started");

		String op = DataUtility.getString(request.getParameter("operation"));
		TimetableModel model = new TimetableModel();
		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			TimetableBean bean = (TimetableBean) populateBean(request);

			try {
				TimetableBean bean1 = model.checkByCourseName(bean.getCourseId(), bean.getExamDate());
				TimetableBean bean2 = model.checkBySubjectName(bean.getCourseId(), bean.getSubjectId(),
						bean.getExamDate());
				TimetableBean bean3 = model.checkBySemester(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(),
						bean.getExamDate());

				if (bean1 == null && bean2 == null && bean3 == null) {
					long pk = model.add(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Timetable added successfully", request);

				} else {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Timetable already exist!", request);

				}
			} catch (DuplicateRecordException e) {
				log.error("DuplicateRecordException in doPost SAVE: ", e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Timetable already exist!", request);
			} catch (ApplicationException e) {
				log.error("ApplicationException in doPost SAVE: ", e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			TimetableBean bean = (TimetableBean) populateBean(request);

			try {
				TimetableBean bean4 = model.checkByExamTime(bean.getCourseId(), bean.getSubjectId(), bean.getSemester(),
						bean.getExamDate(), bean.getExamTime(), bean.getDescription());

				if (id > 0 && bean4 == null) {
					model.update(bean);
					ServletUtility.setBean(bean, request);
					ServletUtility.setSuccessMessage("Timetable updated successfully", request);

				} else {
					ServletUtility.setBean(bean, request);
					ServletUtility.setErrorMessage("Timetable already exist!", request);
					log.warn("Duplicate timetable detected while updating id=" + id);
				}
			} catch (DuplicateRecordException e) {
				log.error("DuplicateRecordException in doPost UPDATE: ", e);
				ServletUtility.setBean(bean, request);
				ServletUtility.setErrorMessage("Timetable already exist!", request);
			} catch (ApplicationException e) {
				log.error("ApplicationException in doPost UPDATE: ", e);
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TIME_TABLE_LIST_CTL, request, response);
			return;

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TIME_TABLE_CTL, request, response);
			return;
		}

		ServletUtility.forward(getView(), request, response);
		log.debug("TimeTableCtl doPost ended");
	}

	/**
	 * Returns the view page for Timetable.
	 */
	@Override
	protected String getView() {
		return ORSView.TIME_TABLE_VIEW;
	}
}
