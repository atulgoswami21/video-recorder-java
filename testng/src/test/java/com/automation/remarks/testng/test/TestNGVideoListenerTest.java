package com.automation.remarks.testng.test;

import com.automation.remarks.testng.VideoListener;
import com.automation.remarks.video.annotations.Video;
import com.automation.remarks.video.recorder.monte.MonteRecorder;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.*;

/**
 * Created by sergey on 18.06.16.
 */
public class TestNGVideoListenerTest extends BaseTest {

  @Test
  @Video
  public void shouldBeOneRecordingOnTestFail() {
    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    listener.onTestFailure(result);
    File file = MonteRecorder.getLastRecording();
    assertTrue(file.exists());
  }

  @Test
  @Video
  public void shouldNotBeRecordingOnTestSuccess() {
    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    listener.onTestSuccess(result);
    File file = MonteRecorder.getLastRecording();
    assertFalse(file.exists());
  }

  @Test
  @Video(name = "new_recording")
  public void shouldBeRecordingWithCustomName() {
    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    listener.onTestFailure(result);
    File file = MonteRecorder.getLastRecording();
    assertTrue(file.exists());
    assertTrue(file.getName().contains("new_recording"));
  }

  @Test
  @Video()
  public void shouldBeRecordingForSuccessfulTestAndSaveModeAll() {
    System.setProperty("video.save.mode", "ALL");
    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    listener.onTestSuccess(result);
    File file = MonteRecorder.getLastRecording();
    assertTrue(file.exists());
    assertTrue(file.getName().contains("shouldBeRecordingForSuccessfulTest"), "Wrong file name");
  }

  @Test
  @Video()
  public void shouldBeRecordingForFailedTestAndSaveModeFailOnly() {
    System.setProperty("video.save.mode", "FAILED_ONLY");
    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    listener.onTestFailure(result);
    File file = MonteRecorder.getLastRecording();
    assertTrue(file.exists());
    assertTrue(file.getName().contains("shouldBeRecordingForFailedTestAndSaveModeFailOnly"), "Wrong file name");
  }

  @Test
  @Video()
  public void shouldNotBeRecordingForSuccessfulTestAndSaveModeFailOnly() {
    System.setProperty("video.save.mode", "FAILED_ONLY");
    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    listener.onTestSuccess(result);
    File file = MonteRecorder.getLastRecording();
    assertFalse(file.exists());
  }

  @Test
  @Video()
  public void shouldNotBeVideoIfDisabledAndRecordModeAll() {
    System.setProperty("video.mode", "ALL");
    System.setProperty("video.enabled", "false");

    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    listener.onTestFailure(result);
    File file = MonteRecorder.getLastRecording();
    assertNotEquals(file.getName(), "shouldNotBeVideoIfDisabledAndRecordModeAll.avi");
  }

  @Test
  @Video()
  public void shouldNotBeRecordingForSuccessTestWithFfmpegAndSaveModeFailOnly() throws InterruptedException {
    System.setProperty("video.save.mode","FAILED_ONLY");
    System.setProperty("recorder.type","FFMPEG");

    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    Thread.sleep(5000);
    listener.onTestSuccess(result);
    File file = MonteRecorder.getLastRecording();
    assertFalse(file.exists());
  }

  @Test
  @Video()
  public void shouldBeRecordingForFailTestWithFfmpegAndSaveModeFailOnly() throws InterruptedException {
    System.setProperty("video.save.mode","FAILED_ONLY");
    System.setProperty("recorder.type","FFMPEG");


    ITestResult result = prepareMock(testMethod);
    VideoListener listener = new VideoListener();
    listener.onTestStart(result);
    Thread.sleep(5000);
    listener.onTestFailure(result);
    File file = MonteRecorder.getLastRecording();
    assertTrue(file.exists());
  }

  @Test
  @Video()
  public void shouldBeRecordingIfCustomVideoAnnotation() {
    System.setProperty("video.save.mode","FAILED_ONLY");
    System.setProperty("recorder.type","FFMPEG");

    ITestResult result = prepareMock(TestNgCustomVideoListenerTest.class, testMethod);
    ITestListener listener = new CustomVideoListener();
    listener.onTestStart(result);
    sleep(5);
    listener.onTestFailure(result);
    File file = MonteRecorder.getLastRecording();
    assertTrue(file.exists());
  }

  static void sleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}

class CustomVideoListener extends VideoListener {

}