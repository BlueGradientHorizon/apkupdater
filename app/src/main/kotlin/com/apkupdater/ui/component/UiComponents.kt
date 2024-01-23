package com.apkupdater.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.apkupdater.R
import com.apkupdater.data.ui.AppInstalled
import com.apkupdater.data.ui.AppUpdate
import com.apkupdater.prefs.Prefs
import com.apkupdater.util.getAppName
import org.koin.androidx.compose.get


@Composable
fun AppImage(app: AppInstalled, onIgnore: (String) -> Unit = {}) = Box {
	LoadingImageApp(app.packageName)
	TextBubble(getAppVersion(app), defaultTextBubbleModifier(), true)
	IgnoreIcon(
		app.ignored,
		{ onIgnore(app.packageName) },
		Modifier
			.align(Alignment.TopEnd)
			.padding(4.dp)
	)
}

@Composable
fun UpdateImage(app: AppUpdate, onInstall: (String) -> Unit = {}) = Box {
	LoadingImageApp(app.packageName)
	TextBubble(getAppVersion(app), defaultTextBubbleModifier(), true)
	InstallProgressIcon(app.isInstalling) { onInstall(app.link) }
	SourceIcon(
		app.source,
		Modifier
			.align(Alignment.TopStart)
			.padding(4.dp)
			.size(28.dp)
	)
}


@Composable
fun SearchImage(app: AppUpdate, onInstall: (String) -> Unit = {}) = Box {
	LoadingImage(app.iconUri)
	TextBubble(getAppVersion(app), defaultTextBubbleModifier(), true)
	InstallProgressIcon(app.isInstalling) { onInstall(app.link) }
	SourceIcon(
		app.source,
		Modifier
			.align(Alignment.TopStart)
			.padding(4.dp)
			.size(28.dp)
	)
}

@Composable
fun InstalledItem(app: AppInstalled, onIgnore: (String) -> Unit = {}) = Column(
	modifier = Modifier.alpha(if (app.ignored) 0.5f else 1f)
) {
	AppImage(app, onIgnore)
	Column(Modifier.padding(top = 4.dp)) {
		ScrollableText { SmallText(app.packageName) }
		MediumTitle(app.name)
	}
}

@Composable
fun UpdateItem(app: AppUpdate, onInstall: (String) -> Unit = {}) = Column {
	UpdateImage(app, onInstall)
	Column(Modifier.padding(top = 4.dp)) {
		ScrollableText { SmallText(app.packageName) }
		MediumTitle(app.name.ifEmpty { LocalContext.current.getAppName(app.packageName) })
	}
}

@Composable
fun SearchItem(app: AppUpdate, onInstall: (String) -> Unit = {}) = Column {
	SearchImage(app, onInstall)
	Column(Modifier.padding(top = 4.dp)) {
		ScrollableText { SmallText(app.packageName) }
		MediumTitle(app.name)
	}
}

@Composable
fun DefaultErrorScreen() = Box(Modifier.fillMaxSize()) {
	HugeText(
		stringResource(R.string.something_went_wrong),
		Modifier.align(Alignment.Center),
		2
	)
}

@Composable
fun getAppVersion(app: AppInstalled): String {
	return if (get<Prefs>().versionNameInsteadCode.get())
		app.version
	else
		app.versionCode.toString()
}

@Composable
fun getAppVersion(app: AppUpdate): String {
	return if (get<Prefs>().versionNameInsteadCode.get())
		app.version
	else
		if (app.versionCode == 0L) "?"
		else app.versionCode.toString()
}

@SuppressLint("ModifierFactoryExtensionFunction")
@Composable
fun BoxScope.defaultTextBubbleModifier() =
	Modifier.align(Alignment.BottomStart).widthIn(max = 120.dp)
