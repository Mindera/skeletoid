# App Rating Dialog

Prompt a dialog for the user to rate your app. There's two ways to use this, using our default dialog or using your own

## Usage

```groovy
dependencies {
    implementation 'com.github.mindera.skeletoid:apprating:1.0.0'
}
```

## Using your own dialog

1. Init with your preferred conditions:
	`AppRatingInitializer.init(context, callback, Pair(maximumCount, timeRange), timeBetweenPrompts)`

	`callback: AppRatingCallback` - Callback that will be called to show the dialog
	`maximumCount: Int` - Number of times the prompt will show within the `timeRange`
	`timeRange: Long` - Time range to reset the number of counts (in days)
	`timeBetweenPrompts: Long` - Minimum time to prompt the dialog again (in days)

	**e.g**, if you want that the minimal time in days to show the user the prompt again to be 30, but you don't want it to show more than 3 times in a year span(365 days), then you will have:
		**maximumCount = 3**
		**timeRange = 365**
		**timeBetweenPrompts = 30**

2. Call `AppRatingInitializer.promptDialog(context)`. This will check if the conditions are met to show the dialog and call the `showRatingDialog()` of the `AppRatingCallback`.
3. Show dialog inside the `showRatingDialog()` of the `AppRatingCallback`.
4. Handle the response and call the following so the conditions are updated:
`AppRatingInitializer.handleDialogResponse(context, AppRatingDialogResponse)`

	Use one of the following depending on the action:
	`AppRatingDialogResponse.RATE_NOW` - this will update the condition as the user just rated the app and will never prompt the dialog again
	`AppRatingDialogResponse.RATE_LATER` - this will schedule a prompt after the `timeBetweenPrompts` value in days as passed
	`AppRatingDialogResponse.NEVER_RATE` - this will never prompt the dialog again

## Using our dialog

1. Init with your preferred conditions, but this time don't pass the callback here:
	`AppRatingInitializer.init(context, Pair(maximumCount, timeRange), timeBetweenPrompts)`

2. Call `AppRatingInitializer.promptDialog(context, fragmentManager)`, passing it a fragment manager. Make sure that the activity of the passed fragmentManager implements the `AppRatingResponseCallback`. This will check if the conditions are met to show the dialog and call the `showRatingDialog()` of the `AppRatingCallback`.

3. Handle the response of the dialog in the `AppRatingDialogResponseCallback` methods:
	`onRateNowClick()`: This will handle the "Ok, sure" button
	`onRateLaterClick()`: This will handle the "Not now" button
	`onNeverRateClick()`: This will handle the "Don't ask me again" button