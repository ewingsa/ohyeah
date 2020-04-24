# Oh Yeah! Application Release Regression

## Contents:

* [On Android Studio](#on-android-studio)
* [On Device](#on-device)
  * [Info Screen](#info-screen)
  * [Set Reminder Screen](#set-reminder-screen)
  * [Conversations Screen](#conversations-screen)
  * [Messages Screen](#messages-screen)
     * [Adding And Deleting Messages](#adding-and-deleting-messages)
     * [Updating](#updating)

 ## On Android Studio

- [ ] The version code and name in the top-level `build.gradle`
should be updated appropriately.
- [ ] All module's unit tests should pass.
- [ ] `gradle ktlint` should be successful. Another way to test is
building the app itself, as its success depends on ktlint's.

## On Device

These are manual quality assurance tests. They should be executed
in order, and on a physical device (no emulators!).

### Info Screen

- [ ] The first screen should say "No reminders set". Press the "?"
button.
- [ ] Rotate the device. The app should not rotate nor crash.
- [ ] Swipe through the tutorial until the end. Swipe from the end
to the beginning. There should be no visual bugs, especially
with the "dot indicators".
- [ ] Press "Open Source Licenses". A dialog box should appear.

### Set Reminder Screen

- [ ] Press the "New Reminder" button on the Home Screen. The time
picker should display the current time. The date picker should
display the current date.
- [ ] Rotate the device. The app should not rotate nor crash.
- [ ] Change the system time to be midnight/12AM. Click the
"New Reminder" button on the Home Screen. The time picker
should display 12 for the hour and "A.M" for the meridiem.
- [ ] Change the system time to be noon/12PM. Click the
"New Reminder" button on the Home Screen. The time picker
should display 12 for the hour and "P.M" for the meridiem.
- [ ] Press the "Select Photo" button. Deny permission.
Nothing should happen.
- [ ] Press the "Select Photo" button. Allow permission. Add a
photo. Confirm that it is displayed on the screen.
- [ ] Write a message that is at least 3 lines of text. Nothing
unexpected should happen, especially with the time wheels.
- [ ] Add Message, "Message", Topic, "Topic", set the time to a
minute or two in the future, and save. Confirm that within a minute
of the set time a notification is sent with the message, topic,
and photo provided. Click the notification. It should navigate to
the appropriate messages screen.
- [ ] Press the "New Reminder" button on the Home Screen. Press
"Select Date" and choose a date that is in the past. Confirm
that the printed date matches the one selected.
- [ ] Add Message, "Past message", do not enter a topic, and save.
Confirm that within a minute a notification is sent with the
message provided, and with the Topic "Oh Yeah!".

### Conversations Screen

- [ ] Rotate the device. The app should not rotate nor crash.
- [ ] Confirm two topics are listed on the home screen, "Topic"
at the top, with "Oh Yeah!" below. The photo for "Oh Yeah!" should
be the app logo, and the photo for "Topic" should be the one
selected above.
- [ ] Add a reminder with Message "Future message", Topic "Topic",
and set its date to be in the future. Save. Add a reminder with
Message "Future topic", Topic "Future Topic", and set its date to be
in the future. Save. On the home screen, the order of conversations
from top to bottom should be "Topic", with preview message "Message"
(not "Future message"), then "Oh Yeah!", then a label that
says "Future Topics:", then "Future Topic".
- [ ] Long press the "Future Topic" topic. A prompt to delete should
appear. Press "cancel". Nothing should happen.
- [ ] Long press the "Future Topic" topic. A prompt to delete should
appear. Press "ok". The topic should be deleted.

### Messages Screen

- [ ] Click the "Topic" conversation in the home screen. The
"Message" message should appear at the top, under a label
corresponding to its date. Then the "Future message"'s date should
be below, followed by the "Future message".
- [ ] Rotate the device. The app should not rotate nor crash.
- [ ] All dates and times should correspond to what they were when
set.

#### Adding And Deleting Messages

- [ ] Press the "New" button at the top of the messages screen (in
the "Topic" conversation). The set reminder screen should have its
Topic set to "Topic", and the photo should be set to
what was previously set. Set the message to be "Past message" and
set its date to be in the past. Save. Should be routed back
to the "Topic" conversation, and "Past message" should be listed
at the top.
- [ ] Add messages with past dates to the "Topic" conversation until
they fill the screen. (Set the message to "A" for consistency.) When
navigating to the "Topic" conversation, the "Message"/most-recent
message should be the bottom-most displayed message. Navigating
down, the "Future message" should be displayed below it.
- [ ] Long press any message. Press "Delete" on the set reminder
screen. A prompt to delete should appear. Press "cancel".
Nothing should happen.
- [ ] Add a reminder for a minute or two in the future.
Long press that reminder. Press "Delete" on the set reminder
screen. A prompt to delete should appear. Press "ok".
The reminder should be deleted and no notification should appear
within at least two minutes of the time it was set.

#### Updating

- [ ] Long press any reminder in the "Topic" conversation. The
time and date should match what was set previously.
- [ ] Change its Topic name to "Updated Topic", change its photo
and save. The "Topic" conversation should now be "Updated Topic" on
the home screen, in its conversation screen, and should be
auto-filled when editing a reminder in that conversation. The
conversation's photo should similarly be updated.
- [ ] Long press the original "Message" reminder in the "Topic"
conversation. Change its name to "Updated message". The
preview text for "Updated Topic" on the home page should be
"Updated message", the reminder in that topic's conversation view
should be updated, and its auto-filled text when editing the
reminder should be updated.
- [ ] Long press a message that is 3 lines long on the set
reminder screen. Nothing unexpected should happen, especially with
the time wheels.
