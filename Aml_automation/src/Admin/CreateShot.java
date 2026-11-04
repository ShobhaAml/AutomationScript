package Admin;

import java.io.File;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import Common.Adminproperty;

/**
 * Emisión tab coverage for superevento shots (spec 2.6).
 * Each test maps to a case or subcomponent in that section: the shot form,
 * render-shot states, render groups, draft / schedule / publish, search and
 * the current-author filter.
 */
public class CreateShot {

    private static final String TIP_NEW_SHOT = "Crear nuevo shot";
    private static final String TIP_PUBLISH = "Publicar ya";
    private static final String TIP_SCHEDULE = "Programar";
    private static final String TIP_CLEAR = "Borrar contenido";
    private static final String TIP_DRAFT = "Guardar como borrador";
    private static final String TIP_PREVIEW = "Vista previa";
    private static final String TIP_EDIT = "Editar";
    private static final String TIP_MORE = "Más acciones";
    private static final String TIP_FOLD = "Plegar";
    private static final String TIP_UNFOLD = "Desplegar";
    private static final String TIP_CANCEL_SCHEDULE = "Cancelar programación";
    private static final String TIP_PICK_SLOT = "Elegir hueco";
    private static final String TIP_TODAY = "Ir a hoy";
    private static final String TIP_GO_TO_DATE = "Ir al día…";
    private static final String TIP_PREV_WEEK = "Ir a la semana anterior";
    private static final String TIP_NEXT_WEEK = "Ir a la semana siguiente";
    private static final String TIP_REFRESH = "Refrescar";
    private static final String TIP_EXPANDED_DEFAULT = "Contenido desplegado por defecto";
    private static final String MENU_EXPANDED = "Ver todo desplegado";
    private static final String MENU_MINE = "Ver sólo tus contenidos";
    private static final String FILTER_TIP_PREFIX = "Ver sólo contenidos creados por";
    private static final String EMPTY_TITLE = "(sin título)";
    private static final String ERR_TIME_REQUIRED = "Error: falta la hora";
    private static final String ERR_TIME_FORMAT = "Error: hay que usar formato hh:mm";
    private static final String ERR_DATE_REQUIRED = "Error: falta la fecha";
    private static final String ERR_DATE_FORMAT = "Error: hay que usar formato dd/mm/aaaa";
    private static final String END_MARKER = "ENDMARKER";
    private static final String PNG_1X1 =
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg==";

    private interface Work {
        void run() throws Exception;
    }

    private WebDriver driver;
    private WebDriver colleague;
    private Properties prop;
    private final Adminproperty admin = new Adminproperty();
    private WebDriverWait wait;
    private JavascriptExecutor js;
    private String activeEmisionUrl;
    private String mainWindow;
    private File pngFile;

    @BeforeClass
    public void setUpClass() throws Exception {
        prop = admin.ReadProperties();
        driver = admin.callproperty(loginUrl(), prop.getProperty("browser", "chrome"));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
        js = (JavascriptExecutor) driver;
        wait = newWait(driver);
        ensureLoggedIn();
        pngFile = File.createTempFile("createshot", ".png");
        Files.write(pngFile.toPath(), Base64.getDecoder().decode(PNG_1X1));
    }

    @BeforeMethod
    public void openCase(Method method) {
        boolean mobile = method.getName().contains("Mobile");
        driver.manage().window().setSize(new Dimension(mobile ? 390 : 1400, mobile ? 844 : 900));
        if (method.getName().contains("WhenOver")) {
            openOver();
        } else {
            openActive();
        }
        mainWindow = driver.getWindowHandle();
    }

    @AfterMethod(alwaysRun = true)
    public void closeExtraWindows() {
        if (driver == null || mainWindow == null) {
            return;
        }
        for (String handle : new ArrayList<String>(driver.getWindowHandles())) {
            if (!handle.equals(mainWindow)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }
        if (driver.getWindowHandles().contains(mainWindow)) {
            driver.switchTo().window(mainWindow);
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDownClass() {
        if (colleague != null) {
            colleague.quit();
        }
        if (driver != null) {
            driver.quit();
        }
        if (pngFile != null) {
            pngFile.delete();
        }
    }

    // ---------------------------------------------------------------------
    // 2.6.1 Emisión tab
    // ---------------------------------------------------------------------

    @Test(priority = 1, description = "2.6.1 EMISION-SUPEREVENTO--ACTIVE allows creating content")
    public void emisionActive_allowsCreatingContent() {
        String token = fillToken();
        Assert.assertTrue(formText().contains(token),
                "Active follow-up should allow writing shot content");
        Assert.assertTrue(isShotFormPresent(), "Shot form should be available while follow-up is active");
    }

    @Test(priority = 2, description = "2.6.1 EMISION-SUPEREVENTO--OVER removes the form and keeps previous shots")
    public void emisionWhenOver_hidesFormAndShowsPreviousContent() {
        Assert.assertTrue(driver.findElements(tip(TIP_PUBLISH)).isEmpty(),
                "Shot form actions should be absent when follow-up is over");
        Assert.assertFalse(isShotFormPresent(), "Shot form should be removed when follow-up is over");
        Assert.assertTrue(groupHeader(publishedName()).isDisplayed(), "Published shots should still be listed");
        Assert.assertTrue(totalShots() > 0, "Shots created before follow-up ended should remain visible");
    }

    @Test(priority = 3, description = "2.6.1.1 title uses the superevento title")
    public void title_matchesSupereventoTitle() {
        String expected = textProp("shot_superevento_title");
        if (expected.isEmpty()) {
            expected = readSupereventoTitleFromEditor();
        }
        String actual = emisionTitle().getText().trim();
        if (!expected.isEmpty()) {
            Assert.assertEquals(actual, expected, "Emisión title should be the superevento title");
        } else {
            Assert.assertFalse(actual.isEmpty(), "Emisión title should show the superevento title");
        }
    }

    @Test(priority = 4, description = "2.6.1.1 empty title shows (sin título)")
    public void title_emptyShowsSinTitulo() {
        String fixture = textProp("shot_empty_title_emision_url");
        if (!fixture.isEmpty()) {
            driver.get(fixture);
            ensureLoggedIn();
            clickEmisionTabIfNeeded();
        } else {
            blankSupereventoTitle();
            clickEmisionTabIfNeeded();
            wait.until(d -> isShotFormPresent());
        }
        Assert.assertEquals(emisionTitle().getText().trim(), EMPTY_TITLE,
                "An empty superevento title should be shown as (sin título)");
    }

    @Test(priority = 5, description = "2.6.1.2 tab-shot is the default form and its tooltip is Crear nuevo shot")
    public void tabShot_tooltipAndShownByDefault() {
        WebElement tab = displayed(tip(TIP_NEW_SHOT));
        assertTooltip(tab, TIP_NEW_SHOT);
        Assert.assertTrue(isShotFormPresent(), "The shot form should be the form shown by default");
        List<WebElement> others = driver.findElements(By.xpath(
                "//*[@role='tab'][contains(.,'RT') or contains(.,'Directísimo') or contains(.,'Directisimo')]"));
        for (WebElement other : others) {
            Assert.assertNotEquals(other.getAttribute("aria-selected"), "true",
                    "RT and directísimo forms should not replace the shot form by default");
        }
    }

    // ---------------------------------------------------------------------
    // 2.6.2 Shot form
    // ---------------------------------------------------------------------

    @Test(priority = 10, description = "2.6.2.1 button-publish tooltip Publicar ya publishes immediately")
    public void buttonPublish_tooltipAndPublishesImmediately() {
        WebElement publish = displayed(tip(TIP_PUBLISH));
        assertTooltip(publish, TIP_PUBLISH);
        String token = fillToken();
        publish.click();
        waitForShot(token, publishedName());
        assertStatusPrefix(token, "Publicado —", "Publicado -");
    }

    @Test(priority = 11, description = "2.6.2.2 button-schedule tooltip Programar shows the schedule form")
    public void buttonSchedule_tooltipAndShowsScheduleForm() {
        WebElement schedule = displayed(tip(TIP_SCHEDULE));
        assertTooltip(schedule, TIP_SCHEDULE);
        schedule.click();
        Assert.assertTrue(scheduleTimeInput().isDisplayed(), "Schedule form should show the time field");
        Assert.assertTrue(scheduleDateInput().isDisplayed(), "Schedule form should show the date field");
    }

    @Test(priority = 12, description = "2.6.2.3 button-clear discards content and does not save a draft")
    public void buttonClear_discardsContentWithoutDraft() {
        WebElement clear = displayed(tip(TIP_CLEAR));
        assertTooltip(clear, TIP_CLEAR);
        String token = fillToken();
        int before = shotCount(draftsName());
        clear.click();
        Assert.assertTrue(isFormEmpty(), "Clear should empty a form that had content");
        Assert.assertEquals(shotCount(draftsName()), before, "Clear should not save a draft");
        Assert.assertFalse(groupContains(draftsName(), token), "Discarded content should not appear in drafts");
    }

    @Test(priority = 13, description = "2.6.2.3 button-clear on an empty form does nothing")
    public void buttonClear_emptyFormDoesNothing() {
        Assert.assertTrue(isFormEmpty(), "Shot form should start empty");
        int before = shotCount(draftsName());
        String notices = noticeTexts();
        clickTip(TIP_CLEAR);
        Assert.assertTrue(isFormEmpty(), "Clear on an empty form should leave it empty");
        Assert.assertEquals(shotCount(draftsName()), before, "Clear on an empty form should not create a draft");
        Assert.assertEquals(noticeTexts(), notices, "Clear on an empty form should not inform the user");
    }

    @Test(priority = 14, description = "2.6.2.4 button-to_draft saves a draft and clears the form")
    public void buttonToDraft_savesAndClears() {
        WebElement draft = displayed(tip(TIP_DRAFT));
        assertTooltip(draft, TIP_DRAFT);
        String token = fillToken();
        draft.click();
        Assert.assertTrue(isFormEmpty(), "Saving a draft should clear the form");
        waitForShot(token, draftsName());
        assertStatusPrefix(token, "Borrador — Editado:", "Borrador - Editado:");
    }

    @Test(priority = 15, description = "2.6.2.4 empty form does not save a draft and informs the user")
    public void buttonToDraft_emptyInformsAndDoesNotSave() {
        Assert.assertTrue(isFormEmpty(), "Shot form should start empty");
        int before = shotCount(draftsName());
        String notices = noticeTexts();
        clickTip(TIP_DRAFT);
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(d -> !noticeTexts().equals(notices));
        Assert.assertFalse(noticeTexts().trim().isEmpty(), "The user should be informed that there is nothing to save");
        Assert.assertEquals(shotCount(draftsName()), before, "An empty form should not be stored as a draft");
    }

    @Test(priority = 16, description = "2.6.2.5 button-preview opens the superevento preview at the shot")
    public void buttonPreview_opensPreviewAtShot() {
        WebElement preview = displayed(tip(TIP_PREVIEW));
        assertTooltip(preview, TIP_PREVIEW);
        String token = fillLong();
        preview.click();
        assertPreviewTab(token);
    }

    @Test(priority = 17, description = "2.6.2.6 mobile toolbar is hidden while the form actions are on screen")
    public void toolbarMainActions_hiddenWhenTopVisible_Mobile() {
        makeFormTall();
        scrollToTop();
        Assert.assertTrue(inViewport(inFormAction(TIP_PUBLISH)), "In-form actions should start on screen");
        Assert.assertNull(stickyToolbarOrNull(), "Sticky action toolbar should stay hidden while those actions are visible");
    }

    @Test(priority = 18, description = "2.6.2.6 mobile toolbar appears with the appbar animation when actions scroll away")
    public void toolbarMainActions_appearsAndMatchesAppbar_Mobile() {
        makeFormTall();
        scrollFormActionsOut();
        WebElement toolbar = stickyToolbarOrNull();
        Assert.assertNotNull(toolbar, "Sticky action toolbar should appear once the form actions leave the screen");
        Assert.assertTrue(inViewport(shotForm()), "The form itself should still be on screen");
        Assert.assertEquals(computed(toolbar, "transition-duration"), computed(appbar(), "transition-duration"),
                "Sticky toolbar should use the appbar transition duration");
        Assert.assertEquals(computed(toolbar, "transition-timing-function"), computed(appbar(), "transition-timing-function"),
                "Sticky toolbar should use the appbar easing");
    }

    @Test(priority = 19, description = "2.6.2.6 mobile toolbar hides again when the whole form leaves the screen")
    public void toolbarMainActions_hidesWhenFormLeaves_Mobile() {
        makeFormTall();
        scrollFormActionsOut();
        Assert.assertNotNull(stickyToolbarOrNull(), "Toolbar should be visible before the form leaves");
        scrollUntilFormLeaves();
        Assert.assertNull(stickyToolbarOrNull(), "Toolbar should disappear once the form is off screen");
    }

    @Test(priority = 20, description = "2.6.2.6 desktop does not show the sticky action toolbar")
    public void toolbarMainActions_notAvailableOnDesktop() {
        makeFormTall();
        scrollFormActionsOut();
        Assert.assertNull(stickyToolbarOrNull(), "Desktop should not show the mobile sticky action toolbar");
    }

    @Test(priority = 21, description = "2.6.2.7 toolbar-add_asset inserts the asset at the cursor")
    public void toolbarAddAsset_insertsAtCursor() throws Exception {
        WebElement editor = editor();
        editor.click();
        editor.sendKeys("AAA");
        editor.sendKeys(Keys.ENTER);
        editor.sendKeys("BBB");
        editor.sendKeys(Keys.chord(Keys.CONTROL, Keys.HOME));
        addImage();
        String html = editor().getAttribute("innerHTML");
        int image = html.toLowerCase(Locale.ROOT).indexOf("<img");
        int tail = html.indexOf("BBB");
        Assert.assertTrue(image >= 0 && tail > image, "Image inserted from the asset toolbar should land at the cursor");
    }

    @Test(priority = 22, description = "2.6.2.8 menu-add_asset appears on a new line and opens the asset toolbar")
    public void menuAddAsset_appearsOnNewLine() {
        WebElement editor = editor();
        editor.click();
        editor.sendKeys("linea");
        int before = shotForm().findElements(By.tagName("button")).size();
        editor.sendKeys(Keys.ENTER);
        WebElement menu = new WebDriverWait(driver, Duration.ofSeconds(8)).until(d -> newlyShownButton(before));
        Assert.assertNotNull(menu, "A new line should reveal the add-asset menu");
        menu.click();
        Assert.assertTrue(assetButtonVisible(), "The add-asset menu should reveal the asset toolbar");
    }

    @Test(priority = 23, description = "2.6.2.8 gap at the bottom of the form lets the user add another line")
    public void shotForm_gapAfterAssets() throws Exception {
        addImage();
        String pad = (String) js.executeScript(
                "return String(Math.round(parseFloat(getComputedStyle(arguments[0]).paddingBottom) || 0));", editor());
        Assert.assertTrue(Integer.parseInt(pad) >= 8,
                "The shot form should keep a gap under its assets so a new line can be added");
    }

    @Test(priority = 24, description = "2.6.2.9 help-shortcuts shows the available keyboard shortcuts")
    public void helpShortcuts_displaysShortcutInformation() {
        WebElement help = findHelp();
        Assert.assertTrue(help.isDisplayed(), "Shortcut help should be visible");
        Assert.assertTrue(help.getText().toLowerCase(Locale.ROOT).contains("atajo") || help.getText().contains("Ctrl"),
                "Shortcut help should describe the available shortcuts");
    }

    @Test(priority = 25, description = "2.6.2.10 drag and drop uploads an image the way homepage shots do")
    public void dragAndDrop_uploadsImage() {
        int before = editor().findElements(By.cssSelector("img")).size();
        dropFile(shotForm());
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                d -> editor().findElements(By.cssSelector("img")).size() > before);
        Assert.assertTrue(editor().findElements(By.cssSelector("img")).size() > before,
                "Dropping an image on the shot form should upload it");
    }

    @Test(priority = 26, description = "2.6.2 text prompt shows an active paste button")
    public void shotForm_textPromptPasteButtonActive() {
        fillToken();
        WebElement paste = displayed(By.xpath(
                "//button[contains(translate(@aria-label,'PEGAR','pegar'),'pegar') or contains(translate(@title,'PEGAR','pegar'),'pegar') or contains(translate(normalize-space(.),'PEGAR','pegar'),'pegar')]"));
        Assert.assertTrue(paste.isDisplayed() && paste.isEnabled(), "Paste button should be active on the text prompt");
    }

    @Test(priority = 27, description = "2.6.2 image asset can be small or normal size")
    public void shotForm_smallAndNormalImageSizes() throws Exception {
        addImage();
        WebElement image = editor().findElement(By.cssSelector("img"));
        image.click();
        WebElement small = sizeButton("peque");
        WebElement normal = sizeButton("normal");
        small.click();
        int smallWidth = editor().findElement(By.cssSelector("img")).getSize().getWidth();
        normal.click();
        int normalWidth = editor().findElement(By.cssSelector("img")).getSize().getWidth();
        Assert.assertTrue(normalWidth > smallWidth, "Normal image should be wider than the small image");
    }

    @Test(priority = 28, description = "2.6.2 a text prompt can sit between two images")
    public void shotForm_textPromptBetweenTwoImages() throws Exception {
        addImage();
        editor().sendKeys("entre imagenes");
        addImage();
        String html = editor().getAttribute("innerHTML").toLowerCase(Locale.ROOT);
        int first = html.indexOf("<img");
        int text = html.indexOf("entre imagenes");
        int second = html.indexOf("<img", first + 4);
        Assert.assertTrue(first >= 0 && text > first && second > text,
                "Text should be editable between two inserted images");
    }

    // ---------------------------------------------------------------------
    // 2.6.3 Shortcuts and 2.6.4 asset toolbars
    // ---------------------------------------------------------------------

    @Test(priority = 30, description = "2.6.3 formatting shortcuts match the homepage shots editor")
    public void shortcuts_boldAndItalicMatchHomepageEditor() {
        WebElement editor = editor();
        editor.click();
        editor.sendKeys("formato");
        editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        editor.sendKeys(Keys.chord(Keys.CONTROL, "b"));
        String html = editor.getAttribute("innerHTML").toLowerCase(Locale.ROOT);
        Assert.assertTrue(html.contains("<strong") || html.contains("<b") || html.contains("font-weight"),
                "Ctrl+B should apply bold, as in the homepage shots editor");
        editor.sendKeys(Keys.chord(Keys.CONTROL, "i"));
        html = editor().getAttribute("innerHTML").toLowerCase(Locale.ROOT);
        Assert.assertTrue(html.contains("<em") || html.contains("<i") || html.contains("italic"),
                "Ctrl+I should apply italic, as in the homepage shots editor");
    }

    @Test(priority = 31, description = "2.6.4 asset toolbar opens the same kind of modal as homepage shots")
    public void assetToolbar_opensHomepageStyleModal() {
        clickAsset("Imagen", "Añadir imagen", "Foto", "Image");
        WebElement dialog = new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='dialog'], input[type='file']")));
        Assert.assertTrue(dialog.isDisplayed(), "Adding an image should open the shared asset modal or file picker");
    }

    // ---------------------------------------------------------------------
    // 2.6.5 Render-shot
    // ---------------------------------------------------------------------

    @Test(priority = 40, description = "2.6.5 RENDER-SHOT--DRAFT")
    public void renderShotDraft_showsDraftCard() {
        String token = saveDraft();
        assertStatusPrefix(token, "Borrador — Editado:", "Borrador - Editado:");
        Assert.assertTrue(shotCard(token).isDisplayed(), "A saved shot should render as a draft card");
    }

    @Test(priority = 41, description = "2.6.5 RENDER-SHOT--SCHEDULED")
    public void renderShotScheduled_showsScheduledCard() {
        String token = scheduleNewShot();
        assertStatusPrefix(token, "Programado —", "Programado -");
    }

    @Test(priority = 42, description = "2.6.5 RENDER-SHOT--PUBLISHED")
    public void renderShotPublished_showsPublishedCard() {
        String token = publishNewShot(false);
        assertStatusPrefix(token, "Publicado —", "Publicado -");
    }

    @Test(priority = 43, description = "2.6.5 RENDER-SHOT--PREVIEW shows the full shot and no edit actions")
    public void renderShotPreview_showsFullContentOnly() {
        String token = fillLong();
        clickTip(TIP_PREVIEW);
        assertPreviewTab(token);
        Assert.assertTrue(driver.getPageSource().contains(END_MARKER), "Preview should show the whole shot");
        Assert.assertTrue(driver.findElements(tip(TIP_EDIT)).isEmpty(), "Preview should not offer edit");
        Assert.assertTrue(driver.findElements(tip(TIP_MORE)).isEmpty(), "Preview should not offer the more-actions menu");
    }

    @Test(priority = 44, description = "2.6.5 RENDER-SHOT--PENDING_CHANGES on a scheduled shot")
    public void renderShotPendingChanges_scheduledShotChangesStatus() {
        String token = scheduleNewShot();
        assertPendingChange(token, "Programado");
    }

    @Test(priority = 45, description = "2.6.5 RENDER-SHOT--PENDING_CHANGES on a published shot")
    public void renderShotPendingChanges_publishedShotChangesStatus() {
        String token = publishNewShot(false);
        assertPendingChange(token, "Publicado");
    }

    @Test(priority = 46, description = "2.6.5 pending changes do not apply to drafts")
    public void renderShotPendingChanges_notAppliedToDraft() {
        String token = saveDraft();
        clickTipWithin(shotCard(token), TIP_EDIT);
        editor().sendKeys(" retoque");
        editor().sendKeys(Keys.TAB);
        Assert.assertTrue(statusOf(token).getText().trim().startsWith("Borrador"),
                "A draft edit should stay a draft rather than a pending-changes status");
    }

    @Test(priority = 47, description = "2.6.5 RENDER-SHOT--HIGHLIGHTED fades after 5s over 2s")
    public void renderShotHighlighted_fadesAfterFiveSeconds() {
        String token = saveDraft();
        assertHighlightedThenFades(token);
    }

    @Test(priority = 48, description = "2.6.5 RENDER-SHOT--EXPANDED is the default after the current author creates a shot")
    public void renderShotExpanded_defaultAfterOwnCreate() {
        String token = saveDraftLong();
        Assert.assertTrue(contentOf(token).getText().contains(END_MARKER),
                "The author's new shot should be expanded and show its full content");
        Assert.assertFalse(isClipped(contentOf(token)), "Expanded content should not be clipped");
    }

    @Test(priority = 49, description = "2.6.5 a later shot by the same author collapses the previously expanded card")
    public void renderShotExpanded_collapsesPreviousOwnShot() {
        String older = saveDraftLong();
        String newer = saveDraftLong();
        Assert.assertFalse(isClipped(contentOf(newer)), "The newest own shot should stay expanded");
        Assert.assertTrue(isClipped(contentOf(older)) || !contentShows(older, END_MARKER),
                "The previously expanded shot should collapse");
    }

    @Test(priority = 50, description = "2.6.5.1 status text, fixed height and ellipsis")
    public void status_textDatetimeFixedHeightAndEllipsis() {
        String draft = saveDraft();
        assertStatusPrefix(draft, "Borrador — Editado:", "Borrador - Editado:");
        assertEllipsisFixedHeight(statusOf(draft));
        String scheduled = scheduleNewShot();
        assertStatusPrefix(scheduled, "Programado —", "Programado -");
        assertEllipsisFixedHeight(statusOf(scheduled));
        String published = publishNewShot(false);
        assertStatusPrefix(published, "Publicado —", "Publicado -");
        assertEllipsisFixedHeight(statusOf(published));
    }

    @Test(priority = 51, description = "2.6.5.2 button-expand grows the card and pushes shots below it")
    public void buttonExpand_growsShotAndPushesShotsBelow() {
        String first = saveDraftLong();
        String second = saveDraftLong();
        int yBefore = cardY(second);
        int heightBefore = contentOf(first).getSize().getHeight();
        expandButton(first).click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                d -> contentOf(first).getSize().getHeight() > heightBefore);
        Assert.assertTrue(cardY(second) > yBefore, "Expanding a shot should push the shots below it downward");
    }

    @Test(priority = 52, description = "2.6.5.3 collapsed content has a fixed height and hides overflow")
    public void content_collapsedFixedHeightOverflowHidden() {
        String older = saveDraftLong();
        saveDraftLong();
        WebElement content = contentOf(older);
        String height = computed(content, "height");
        String maxHeight = computed(content, "max-height");
        Assert.assertTrue(!"auto".equals(height) || (maxHeight != null && !"none".equals(maxHeight)),
                "Collapsed content should use a fixed height");
        String overflow = computed(content, "overflow") + computed(content, "overflow-y");
        Assert.assertTrue(overflow.contains("hidden"), "Collapsed content should hide overflow");
    }

    @Test(priority = 53, description = "2.6.5.3 render-shot content is not editable or actionable")
    public void content_notEditableOrActionable() {
        String token = fillToken();
        editor().sendKeys(" https://example.com/shot-" + token);
        clickTip(TIP_DRAFT);
        waitForShot(token, draftsName());
        WebElement content = contentOf(token);
        Assert.assertNotEquals(content.getAttribute("contenteditable"), "true",
                "Render-shot content should not be editable");
        assertNotActionable(content);
        saveDraftLong();
        assertNotActionable(contentOf(token));
    }

    @Test(priority = 54, description = "2.6.5.3 collapsed long text fills the visible area and clips the rest")
    public void content_longTextFillsCollapsedArea() {
        String older = saveDraftLong();
        saveDraftLong();
        WebElement content = contentOf(older);
        Assert.assertTrue(content.getText().contains(older), "Collapsed content should keep the start of the text");
        Assert.assertTrue(isClipped(content) || !content.getText().contains(END_MARKER),
                "Collapsed content should not show the whole long text");
    }

    @Test(priority = 55, description = "2.6.5.3 short text also includes the following asset")
    public void content_shortTextIncludesSecondAsset() throws Exception {
        String token = fillToken();
        addImage();
        clickTip(TIP_DRAFT);
        waitForShot(token, draftsName());
        saveDraft();
        WebElement content = contentOf(token);
        Assert.assertTrue(content.getText().contains(token), "Short text should remain visible");
        Assert.assertFalse(content.findElements(By.cssSelector("img")).isEmpty(),
                "A second asset should be included when the text is shorter than the visible area");
    }

    @Test(priority = 56, description = "2.6.5.3 a non-text first asset fills the collapsed card")
    public void content_nonTextFirstAssetStaysWithinCollapsedArea() throws Exception {
        addImage();
        String token = "tail" + System.currentTimeMillis();
        editor().sendKeys(token + " segundo asset");
        clickTip(TIP_DRAFT);
        waitForShot(token, draftsName());
        saveDraft();
        WebElement content = contentOf(token);
        Assert.assertFalse(content.findElements(By.cssSelector("img")).isEmpty(),
                "The first asset should be the image");
        assertOutsideVisibleBox(content, token);
    }

    @Test(priority = 57, description = "2.6.5.3 expanded content shows the whole shot")
    public void content_expandedShowsWholeContent() {
        String token = saveDraftLong();
        WebElement content = contentOf(token);
        Assert.assertTrue(content.getText().contains(END_MARKER), "Expanded content should show the whole shot");
        Assert.assertFalse(isClipped(content), "Expanded height should adapt to the content");
        assertNotActionable(content);
    }

    @Test(priority = 58, description = "2.6.5.3 content background uses a pattern and a gradient")
    public void content_backgroundPatternAndGradient() {
        String token = saveDraft();
        assertPatternAndGradient(contentOf(token));
    }

    @Test(priority = 59, description = "2.6.5.4 author avatar alt and tooltip are the author name")
    public void author_altAndTooltipAreAuthorName() {
        String token = saveDraft();
        WebElement avatar = authorAvatar(token);
        String alt = avatar.getAttribute("alt");
        Assert.assertNotNull(alt, "Author image should have alt text");
        Assert.assertFalse(alt.trim().isEmpty(), "Author alt text should be the author name");
        String tip = firstNonEmpty(avatar.getAttribute("title"), avatar.getAttribute("aria-label"));
        if (tip.isEmpty()) {
            WebElement parent = avatar.findElement(By.xpath(".."));
            tip = firstNonEmpty(parent.getAttribute("title"), parent.getAttribute("aria-label"));
        }
        Assert.assertEquals(tip, alt, "Author tooltip should match the alt text");
    }

    @Test(priority = 60, description = "2.6.5.5 render-shot preview matches the form preview")
    public void renderShotPreviewButton_opensPreviewLikeForm() {
        String token = saveDraftLong();
        clickTipWithin(shotCard(token), TIP_PREVIEW);
        assertPreviewTab(token);
    }

    @Test(priority = 61, description = "2.6.5.6 button-edit tooltip Editar loads the shot in the form")
    public void buttonEdit_tooltipStartsEdit() {
        String token = saveDraft();
        WebElement edit = shotCard(token).findElement(tipRelative(TIP_EDIT));
        assertTooltip(edit, TIP_EDIT);
        edit.click();
        Assert.assertTrue(formText().contains(token), "Edit should load this shot into the shot form");
    }

    @Test(priority = 62, description = "2.6.5.7 button-menu tooltip Más acciones opens secondary actions")
    public void buttonMenu_tooltipOpensMoreActions() {
        String token = saveDraft();
        WebElement menu = shotCard(token).findElement(tipRelative(TIP_MORE));
        assertTooltip(menu, TIP_MORE);
        menu.click();
        Assert.assertTrue(menuContainerVisible(), "Más acciones should open the secondary menu");
    }

    @Test(priority = 63, description = "2.6.5.8 send to draft is only offered for published and scheduled shots")
    public void menuSendToDraft_onlyForPublishedAndScheduled() {
        String draft = saveDraft();
        openMenu(draft);
        Assert.assertNull(menuItem(toDraftLabels()), "Draft cards should not offer send-to-draft");
        closeMenu();
        String published = publishNewShot(false);
        openMenu(published);
        Assert.assertNotNull(menuItem(toDraftLabels()), "Published cards should offer send-to-draft");
        closeMenu();
        String scheduled = scheduleNewShot();
        openMenu(scheduled);
        Assert.assertNotNull(menuItem(toDraftLabels()), "Scheduled cards should offer send-to-draft");
    }

    @Test(priority = 64, description = "2.6.5.9 delete is only offered for drafts")
    public void menuDelete_onlyForDraft() {
        String draft = saveDraft();
        openMenu(draft);
        Assert.assertNotNull(menuItem(deleteLabels()), "Draft cards should offer delete");
        closeMenu();
        String published = publishNewShot(false);
        openMenu(published);
        Assert.assertNull(menuItem(deleteLabels()), "Published cards should not offer delete");
    }

    @Test(priority = 65, description = "2.6.5.8 send to draft moves a published shot into drafts")
    public void menuSendToDraft_movesShotToDrafts() {
        String token = publishNewShot(false);
        openMenu(token);
        menuItem(toDraftLabels()).click();
        confirmIfDialog();
        waitForShot(token, draftsName());
        assertStatusPrefix(token, "Borrador — Editado:", "Borrador - Editado:");
    }

    @Test(priority = 66, description = "2.6.5.9 delete removes a draft")
    public void menuDelete_removesDraft() {
        String token = saveDraft();
        openMenu(token);
        menuItem(deleteLabels()).click();
        confirmIfDialog();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> !pageHas(token));
        Assert.assertFalse(pageHas(token), "Deleted draft should leave the Emisión tab");
    }

    // ---------------------------------------------------------------------
    // 2.6.6 Render groups
    // ---------------------------------------------------------------------

    @Test(priority = 70, description = "2.6.6 active follow-up lists render groups under the shot form")
    public void renderGroups_belowShotFormWhenActive() {
        int formBottom = shotForm().getLocation().getY() + shotForm().getSize().getHeight();
        Assert.assertTrue(groupHeader(publishedName()).getLocation().getY() >= formBottom - 8,
                "Render groups should sit below the shot form during active follow-up");
        Assert.assertTrue(isGroupOpen(publishedName()), "Published group should start open");
        Assert.assertTrue(isGroupOpen(scheduledName()), "Scheduled group should start open");
        Assert.assertFalse(isGroupOpen(draftsName()), "Drafts group should start collapsed");
    }

    @Test(priority = 71, description = "2.6.6 over follow-up lists render groups under the status message")
    public void renderGroupsWhenOver_sitBelowStatusMessage() {
        WebElement status = overStatus();
        Assert.assertTrue(groupHeader(publishedName()).getLocation().getY() >= status.getLocation().getY(),
                "Render groups should sit below the status message when follow-up is over");
    }

    @Test(priority = 72, description = "2.6.6 published group is reverse chronological and grouped by day")
    public void publishedGroup_reverseChronologicalByDay() {
        Assert.assertTrue(isGroupOpen(publishedName()), "Published group should start open");
        String older = publishNewShot(false);
        String newer = publishNewShot(false);
        Assert.assertTrue(cardY(newer) < cardY(older), "Most recently published shot should be first");
        List<WebElement> dividers = dateDividers(publishedName());
        Assert.assertFalse(dividers.isEmpty(), "Published shots should be grouped under a day divider");
        Assert.assertTrue(dateDividers(draftsName()).isEmpty(), "Day dividers belong only to the published group");
        Assert.assertTrue(dateDividers(scheduledName()).isEmpty(), "Day dividers belong only to the published group");
        for (WebElement card : shotCards(publishedName())) {
            boolean underDivider = false;
            for (WebElement divider : dividers) {
                if (divider.getLocation().getY() <= card.getLocation().getY()) {
                    underDivider = true;
                }
            }
            Assert.assertTrue(underDivider, "Each published shot should appear under its day divider");
        }
    }

    @Test(priority = 73, description = "2.6.6 published pagination keeps the form and the other groups")
    public void publishedGroup_paginationKeepsRestOfPage() {
        WebElement previous = pager(publishedName(), true);
        WebElement next = pager(publishedName(), false);
        Assert.assertNotNull(previous, "Published group should have a previous-page button");
        Assert.assertNotNull(next, "Published group should have a next-page button");
        Assert.assertTrue(isDisabled(previous), "Previous page should be disabled on the first page");
        Assert.assertTrue(pager(scheduledName(), true) == null, "Scheduled group should not paginate");
        Assert.assertTrue(pager(draftsName(), false) == null, "Drafts group should not paginate");
        if (!isDisabled(next)) {
            next.click();
            Assert.assertTrue(isShotFormPresent(), "Shot form should stay visible on later published pages");
            Assert.assertTrue(groupHeader(draftsName()).isDisplayed(), "Drafts group should stay visible");
            Assert.assertTrue(groupHeader(scheduledName()).isDisplayed(), "Scheduled group should stay visible");
            Assert.assertFalse(isDisabled(pager(publishedName(), true)), "Previous page should enable after moving forward");
        }
        WebElement lastNext = pager(publishedName(), false);
        int guard = 0;
        while (lastNext != null && !isDisabled(lastNext) && guard < 8) {
            lastNext.click();
            lastNext = pager(publishedName(), false);
            guard++;
        }
        Assert.assertNotNull(lastNext, "Published group should keep its next-page button");
        Assert.assertTrue(isDisabled(lastNext), "Next page should be disabled on the last page");
    }

    @Test(priority = 74, description = "2.6.6 scheduled group is open and reverse chronological by schedule date")
    public void scheduledGroup_reverseChronological() {
        Assert.assertTrue(isGroupOpen(scheduledName()), "Scheduled group should start open");
        String earlier = scheduleAt(browserTimePlus(90));
        String later = scheduleAt(browserTimePlus(180));
        Assert.assertTrue(cardY(later) < cardY(earlier),
                "The shot scheduled furthest ahead should be listed first");
    }

    @Test(priority = 75, description = "2.6.6 drafts group is collapsed and reverse chronological by update")
    public void draftsGroup_collapsedReverseChronologicalByUpdate() {
        Assert.assertFalse(isGroupOpen(draftsName()), "Drafts group should start collapsed");
        String older = saveDraft();
        String newer = saveDraft();
        setGroupOpen(draftsName(), true);
        Assert.assertTrue(cardY(newer) < cardY(older), "The most recently updated draft should be listed first");
    }

    @Test(priority = 76, description = "2.6.6.1 header shows the group name and count and toggles the group")
    public void header_showsNameCountAndToggles() {
        WebElement header = groupHeader(publishedName());
        Assert.assertTrue(header.getText().toLowerCase(Locale.ROOT).contains(publishedName().toLowerCase(Locale.ROOT)),
                "Header should name the group");
        Matcher count = Pattern.compile("(\\d+)").matcher(header.getText());
        if (count.find()) {
            Assert.assertEquals(Integer.parseInt(count.group(1)), shotCount(publishedName()),
                    "Header count should match the shots in the group");
        }
        boolean open = isGroupOpen(publishedName());
        header.click();
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(d -> isGroupOpen(publishedName()) != open);
        Assert.assertNotEquals(isGroupOpen(publishedName()), open, "Clicking the header should toggle the group");
    }

    @Test(priority = 77, description = "2.6.6.1 collapse choices persist when the user comes back")
    public void header_collapseStatePersistsAfterReload() {
        try {
            setGroupOpen(draftsName(), true);
            setGroupOpen(publishedName(), false);
            driver.navigate().refresh();
            ensureLoggedIn();
            clickEmisionTabIfNeeded();
            wait.until(d -> isShotFormPresent());
            Assert.assertTrue(isGroupOpen(draftsName()), "Drafts should stay open after a return visit");
            Assert.assertFalse(isGroupOpen(publishedName()), "Published should stay collapsed after a return visit");
        } finally {
            restoreGroupDefaults();
        }
    }

    @Test(priority = 78, description = "2.6.6.2 toggle tooltips are Plegar and Desplegar")
    public void buttonToggle_tooltipsFollowState() {
        WebElement toggle = toggleButton(publishedName());
        if (isGroupOpen(publishedName())) {
            assertTooltip(toggle, TIP_FOLD);
            toggle.click();
            assertTooltip(toggleButton(publishedName()), TIP_UNFOLD);
        } else {
            assertTooltip(toggle, TIP_UNFOLD);
            toggle.click();
            assertTooltip(toggleButton(publishedName()), TIP_FOLD);
        }
    }

    @Test(priority = 79, description = "2.6.6.3 go-to-date exists only on published and toggles the calendar")
    public void goToDate_onlyOnPublishedAndTogglesCalendar() {
        Assert.assertNotNull(goToDateButton(publishedName()), "Published group should offer Ir al día");
        Assert.assertNull(goToDateButton(scheduledName()), "Scheduled group should not offer Ir al día");
        Assert.assertNull(goToDateButton(draftsName()), "Drafts group should not offer Ir al día");
        goToDateButton(publishedName()).click();
        Assert.assertTrue(calendarPopup().isDisplayed(), "Ir al día should open the calendar");
        goToDateButton(publishedName()).click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> driver.findElements(calendarBy()).isEmpty()
                || !driver.findElements(calendarBy()).get(0).isDisplayed());
    }

    @Test(priority = 80, description = "2.6.6.4 calendar enables only days that have published shots")
    public void calendar_onlyDaysWithPublishedShotsAreActive() {
        publishNewShot(false);
        goToDateButton(publishedName()).click();
        WebElement calendar = calendarPopup();
        String today = (String) js.executeScript("return String(new Date().getDate());");
        WebElement todayCell = calendar.findElement(By.xpath(".//button[normalize-space(.)=" + q(today) + "]"));
        Assert.assertTrue(todayCell.isEnabled() && !"true".equals(todayCell.getAttribute("aria-disabled")),
                "Today should be active after publishing a shot today");
        WebElement nextMonth = calendar.findElement(By.cssSelector(
                "button[aria-label*='iguiente'], button[aria-label*='Next'], .MuiPickersArrowSwitcher-nextIconButton"));
        nextMonth.click();
        List<WebElement> days = calendar.findElements(By.cssSelector("button.MuiPickersDay-root, button[role='gridcell']"));
        Assert.assertFalse(days.isEmpty(), "Calendar should list the days of the month");
        for (WebElement day : days) {
            if (!day.isDisplayed()) {
                continue;
            }
            boolean disabled = !day.isEnabled()
                    || "true".equals(day.getAttribute("aria-disabled"))
                    || String.valueOf(day.getAttribute("class")).contains("disabled")
                    || String.valueOf(day.getAttribute("class")).contains("Mui-disabled");
            Assert.assertTrue(disabled, "Days without published shots should be inactive");
        }
    }

    @Test(priority = 81, description = "2.6.6.8 Ver todo desplegado expands content without changing collapse")
    public void config_verTodoDesplegadoTogglesAndKeepsCollapse() {
        try {
            String older = saveDraftLong();
            saveDraftLong();
            setGroupOpen(scheduledName(), false);
            setGroupOpen(draftsName(), true);
            Assert.assertTrue(isClipped(contentOf(older)) || !contentShows(older, END_MARKER),
                    "Older shot should use the collapsed card before the option is enabled");
            openConfigMenu();
            WebElement option = menuItem(MENU_EXPANDED);
            Assert.assertNotNull(option, MENU_EXPANDED);
            assertTooltip(option, TIP_EXPANDED_DEFAULT);
            Assert.assertFalse(option.findElements(By.cssSelector("svg, img")).isEmpty(),
                    "Ver todo desplegado should include its admin icon");
            option.click();
            Assert.assertFalse(isGroupOpen(scheduledName()),
                    "Configuration should leave collapsed groups collapsed");
            Assert.assertFalse(isClipped(contentOf(older)), "Ver todo desplegado should expand every shot");
            openConfigMenu();
            menuItem(MENU_EXPANDED).click();
            Assert.assertTrue(isClipped(contentOf(older)) || !contentShows(older, END_MARKER),
                    "Choosing the option again should restore the default cards");
            Assert.assertFalse(isGroupOpen(scheduledName()),
                    "Turning the option off should also leave group collapse unchanged");
        } finally {
            restoreGroupDefaults();
        }
    }

    @Test(priority = 82, description = "2.6.6.8 and 2.6.12 filter to the current author without uncollapsing groups")
    public void config_filterCurrentAuthorKeepsCollapse() throws Exception {
        try {
            String mine = publishNewShot(false);
            final String[] theirs = new String[1];
            withColleague(new Work() {
                public void run() {
                    theirs[0] = publishNewShot(false);
                }
            });
            waitForShot(theirs[0], publishedName());
            setGroupOpen(publishedName(), false);
            String beforeHtml = groupHeader(publishedName()).getAttribute("outerHTML");
            openConfigMenu();
            WebElement option = menuItem(MENU_MINE);
            Assert.assertNotNull(option, MENU_MINE);
            assertTooltipContains(option, FILTER_TIP_PREFIX);
            option.click();
            Assert.assertFalse(isGroupOpen(publishedName()), "Author filter should not uncollapse groups");
            Assert.assertNotEquals(groupHeader(publishedName()).getAttribute("outerHTML"), beforeHtml,
                    "Group headers should show the filter icon");
            setGroupOpen(publishedName(), true);
            Assert.assertTrue(groupContains(publishedName(), mine), "Own shots should remain");
            Assert.assertFalse(groupContains(publishedName(), theirs[0]), "Other authors should be hidden");
            openConfigMenu();
            menuItem(MENU_MINE).click();
            setGroupOpen(publishedName(), true);
            Assert.assertTrue(groupContains(publishedName(), theirs[0]),
                    "Choosing the filter again should restore every author");
        } finally {
            restoreGroupDefaults();
        }
    }

    @Test(priority = 83, description = "2.6.6.9 floating new-shot button stays hidden while the form or paste fabs are visible")
    public void buttonNew_hiddenWhileFormVisible_Mobile() {
        fillToken();
        scrollToTop();
        Assert.assertTrue(inViewport(shotForm()), "Shot form should be on screen");
        Assert.assertNull(newShotFabOrNull(), "New-shot button should be hidden while the form is visible");
        editor().sendKeys(Keys.ENTER);
        if (pasteFabVisible() && addAssetFabVisible()) {
            Assert.assertNull(newShotFabOrNull(),
                    "New-shot button should be hidden while paste and add-asset fabs are on screen");
        }
    }

    @Test(priority = 84, description = "2.6.6.9 floating new-shot button saves a draft like Guardar como borrador")
    public void buttonNew_savesDraftWhenFormOffscreen_Mobile() {
        String token = fillToken();
        scrollUntilFormLeaves();
        WebElement fab = newShotFabOrNull();
        Assert.assertNotNull(fab, "New-shot button should appear once the form is off screen");
        fab.click();
        Assert.assertTrue(isFormEmpty() || !formText().contains(token),
                "The floating button should clear the form the way to-draft does");
        waitForShot(token, draftsName());
    }

    // ---------------------------------------------------------------------
    // 2.6.7 To draft
    // ---------------------------------------------------------------------

    @Test(priority = 90, description = "2.6.7 saving a draft highlights and expands it and keeps focus on the empty form")
    public void toDraft_highlightedExpandedAndFocusStays() {
        String token = saveDraftLong();
        Assert.assertTrue(contentOf(token).getText().contains(END_MARKER), "The new draft should be expanded");
        WebElement active = driver.switchTo().activeElement();
        Boolean inside = (Boolean) js.executeScript("return arguments[0].contains(arguments[1]);", shotForm(), active);
        Assert.assertTrue(Boolean.TRUE.equals(inside), "Focus should remain on the emptied shot form");
        assertHighlightedThenFades(token);
    }

    @Test(priority = 91, description = "2.6.7 snackbar-saved scrolls smoothly to the new draft")
    public void snackbarSaved_scrollsSmoothlyToDraft() {
        String token = fillLong();
        clickTip(TIP_DRAFT);
        WebElement snackbar = snackbar();
        Assert.assertTrue(snackbar.isDisplayed(), "Saving a draft should show a confirmation snackbar");
        js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
        WebElement link = snackbar.findElement(By.xpath(".//a|.//button"));
        assertSmoothScroll(link);
        setGroupOpen(draftsName(), true);
        Assert.assertTrue(inViewport(shotCard(token)), "The snackbar link should scroll to the new draft");
    }

    // ---------------------------------------------------------------------
    // 2.6.8 Scheduling
    // ---------------------------------------------------------------------

    @Test(priority = 100, description = "2.6.8 current author schedule is highlighted and expanded like to-draft")
    public void scheduling_currentAuthorHighlightedExpanded() {
        String token = scheduleLong();
        Assert.assertTrue(contentShows(token, END_MARKER), "Own scheduled shot should be expanded");
        assertHighlightedThenFades(token);
    }

    @Test(priority = 101, description = "2.6.8 a colleague schedule is not expanded")
    public void scheduling_otherAuthorNotExpanded() throws Exception {
        final String[] token = new String[1];
        withColleague(new Work() {
            public void run() {
                token[0] = scheduleLong();
            }
        });
        waitForShot(token[0], scheduledName());
        Assert.assertTrue(isClipped(contentOf(token[0])) || !contentShows(token[0], END_MARKER),
                "A shot scheduled by another author should not be expanded");
    }

    @Test(priority = 102, description = "2.6.8.1 Cancelar programación hides the schedule form")
    public void scheduleCancel_hidesForm() {
        openSchedule();
        WebElement cancel = displayed(tip(TIP_CANCEL_SCHEDULE));
        assertTooltip(cancel, TIP_CANCEL_SCHEDULE);
        cancel.click();
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> !scheduleFormVisible());
        Assert.assertFalse(scheduleFormVisible(), "Cancel should hide the schedule form");
    }

    @Test(priority = 103, description = "2.6.8.2 time has no widget and defaults to five minutes ahead")
    public void inputTime_noWidgetDefaultFiveMinutes() {
        openSchedule();
        WebElement time = scheduleTimeInput();
        Assert.assertNotEquals(time.getAttribute("type"), "time", "Time field should not use a time widget");
        time.click();
        try {
            new WebDriverWait(driver, Duration.ofSeconds(1)).until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".MuiClock-root, .MuiMultiSectionDigitalClock-root")));
            Assert.fail("Focusing the time field should not open a clock widget");
        } catch (TimeoutException expected) {
            // No clock was shown.
        }
        int actual = minutesOf(time.getAttribute("value"));
        int expected = minutesOf(browserTimePlus(5));
        int diff = Math.abs(actual - expected);
        diff = Math.min(diff, (24 * 60) - diff);
        Assert.assertTrue(diff <= 2, "Default time should be about five minutes after the button was clicked");
    }

    @Test(priority = 104, description = "2.6.8.2 time is required and must use hh:mm")
    public void inputTime_requiredAndFormatErrors() {
        openSchedule();
        WebElement time = scheduleTimeInput();
        setInput(time, "");
        leaveField(time);
        assertErrorEventually(ERR_TIME_REQUIRED);
        openSchedule();
        setInput(scheduleTimeInput(), "25:99");
        leaveField(scheduleTimeInput());
        assertErrorEventually(ERR_TIME_FORMAT);
    }

    @Test(priority = 105, description = "2.6.8.3 date defaults to today and uses the calendar widget")
    public void inputDate_defaultTodayAndCalendarWidget() {
        openSchedule();
        WebElement date = scheduleDateInput();
        String shown = firstNonEmpty(date.getAttribute("value"), date.getText());
        String today = (String) js.executeScript(
                "const d = new Date(); const p = n => ('0' + n).slice(-2);"
                        + "return p(d.getDate()) + '/' + p(d.getMonth() + 1) + '/' + d.getFullYear();");
        String[] parts = today.split("/");
        Assert.assertTrue(shown.contains(parts[0]) && shown.contains(parts[1]) && shown.contains(parts[2]),
                "Default date should be today (" + today + ") but was " + shown);
        date.click();
        Assert.assertTrue(calendarPopup().isDisplayed(), "Date field should open the MUI calendar");
    }

    @Test(priority = 106, description = "2.6.8.3 date is required and must use dd/mm/aaaa when the field is left")
    public void inputDate_requiredAndFormatErrorsOnBlur() {
        openSchedule();
        WebElement date = scheduleDateInput();
        setInput(date, "");
        leaveField(date);
        assertError(ERR_DATE_REQUIRED);
        openSchedule();
        setInput(scheduleDateInput(), "2026/11/05");
        leaveField(scheduleDateInput());
        assertError(ERR_DATE_FORMAT);
    }

    @Test(priority = 107, description = "2.6.8.4 Elegir hueco opens the slots table")
    public void buttonSlot_opensSlotsTable() {
        openSchedule();
        WebElement pick = displayed(tip(TIP_PICK_SLOT));
        assertTooltip(pick, TIP_PICK_SLOT);
        pick.click();
        WebElement dialog = new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            WebElement slots = slotsDialogOrNull();
            return slots != null && slots.isDisplayed() ? slots : null;
        });
        Assert.assertTrue(dialog.isDisplayed(), "Elegir hueco should open the slots table");
        Assert.assertFalse(dialog.findElements(tipRelative(TIP_TODAY)).isEmpty(), "Slots table should offer Ir a hoy");
    }

    @Test(priority = 108, description = "2.6.8.5 schedule snackbar links to the preview and stays below the appbar")
    public void scheduleSnackbar_linkOpensPreviewBelowAppbar() {
        String token = scheduleNewShot();
        WebElement snackbar = snackbar();
        assertBelowAppbar(snackbar);
        snackbar.findElement(By.xpath(".//a|.//button")).click();
        assertPreviewTab(token);
    }

    @Test(priority = 109, description = "2.6.8.6 snackbar tells the user a colleague scheduled a shot")
    public void scheduleSnackbarColleague_textAndPreviewLink() throws Exception {
        final String[] token = new String[1];
        withColleague(new Work() {
            public void run() {
                token[0] = scheduleNewShot();
            }
        });
        WebElement snackbar = new WebDriverWait(driver, Duration.ofSeconds(25)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(.,'ha programado para')]")));
        Assert.assertTrue(Pattern.compile(".+ ha programado para .+Ver vista previa", Pattern.DOTALL)
                .matcher(snackbar.getText()).find(), "Colleague snackbar should name the author, time and preview link");
        snackbar.findElement(By.xpath(".//a[contains(.,'vista previa')]|.//button[contains(.,'vista previa')]")).click();
        assertPreviewTab(token[0]);
    }

    // ---------------------------------------------------------------------
    // 2.6.9 Slots table
    // ---------------------------------------------------------------------

    @Test(priority = 110, description = "2.6.9.1 Ir a hoy returns the table to today")
    public void slotsToday_returnsToToday() {
        WebElement dialog = openSlots();
        WebElement today = dialog.findElement(tipRelative(TIP_TODAY));
        assertTooltip(today, TIP_TODAY);
        String original = firstColumn(dialog);
        dialog.findElement(tipRelative(TIP_NEXT_WEEK)).click();
        Assert.assertNotEquals(firstColumn(dialog), original, "Next week should move the first day");
        dialog.findElement(tipRelative(TIP_TODAY)).click();
        Assert.assertEquals(firstColumn(dialog), original, "Ir a hoy should restore today as the first day");
    }

    @Test(priority = 111, description = "2.6.9.2 Ir al día opens a calendar for the first column")
    public void slotsGoToDate_opensCalendar() {
        WebElement dialog = openSlots();
        WebElement go = dialog.findElement(tipRelative(TIP_GO_TO_DATE));
        assertTooltip(go, TIP_GO_TO_DATE);
        go.click();
        Assert.assertTrue(calendarPopup().isDisplayed(), "Ir al día should open the calendar widget");
    }

    @Test(priority = 112, description = "2.6.9.3 and 2.6.9.4 week buttons move the table by one week")
    public void slotsPrevNext_weekTooltips() {
        WebElement dialog = openSlots();
        WebElement next = dialog.findElement(tipRelative(TIP_NEXT_WEEK));
        WebElement previous = dialog.findElement(tipRelative(TIP_PREV_WEEK));
        assertTooltip(next, TIP_NEXT_WEEK);
        assertTooltip(previous, TIP_PREV_WEEK);
        String original = firstColumn(dialog);
        next.click();
        String moved = firstColumn(dialog);
        Assert.assertNotEquals(moved, original, "Ir a la semana siguiente should show the next week");
        dialog.findElement(tipRelative(TIP_PREV_WEEK)).click();
        Assert.assertEquals(firstColumn(dialog), original, "Ir a la semana anterior should return to the previous week");
    }

    @Test(priority = 113, description = "2.6.9.5 Refrescar reloads shots scheduled by someone else")
    public void slotsRefresh_reloadsColleagueShot() throws Exception {
        WebElement dialog = openSlots();
        WebElement refresh = dialog.findElement(tipRelative(TIP_REFRESH));
        assertTooltip(refresh, TIP_REFRESH);
        final String[] token = new String[1];
        withColleague(new Work() {
            public void run() {
                token[0] = scheduleNewShot();
            }
        });
        WebElement dialogAgain = slotsDialogOrNull();
        Assert.assertNotNull(dialogAgain, "Slots table should stay open while a colleague schedules a shot");
        dialogAgain.findElement(tipRelative(TIP_REFRESH)).click();
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> driver.getPageSource().contains(token[0]));
        Assert.assertTrue(driver.getPageSource().contains(token[0]),
                "Refresh should show a shot scheduled while the table was open");
    }

    @Test(priority = 114, description = "2.6.9.6 a busy slot shows one shot time and excerpt")
    public void slotBusy_singleShotShowsTimeAndExcerpt() {
        String time = browserTimePlus(120);
        String token = scheduleAt(time);
        WebElement dialog = openSlots();
        WebElement cell = busyCell(dialog, token);
        Assert.assertTrue(cell.getText().contains(time), "Busy slot should show the publish time");
        Assert.assertTrue(cell.getText().contains(token), "Busy slot should show the shot excerpt");
    }

    @Test(priority = 115, description = "2.6.9.6 several shots in one slot show a count and a hover overlay")
    public void slotBusy_multipleShowsCountAndHoverOverlay() {
        String time = browserTimePlus(150);
        String first = scheduleAt(time);
        String second = scheduleAt(time);
        WebElement dialog = openSlots();
        WebElement cell = busyCell(dialog, "contenidos");
        Assert.assertTrue(cell.getText().matches("(?s).*\\d+ contenidos.*"),
                "A shared slot should show how many shots it contains");
        new Actions(driver).moveToElement(cell).perform();
        WebElement overlay = new WebDriverWait(driver, Duration.ofSeconds(5)).until(
                ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[role='tooltip'], [class*='overlay'], [class*='popover']")));
        String overlayText = overlay.getText();
        Assert.assertTrue(overlayText.contains(first) && overlayText.contains(second),
                "Hover should list every shot in the slot");
        Assert.assertTrue(overlayText.contains(time), "Each overlay line should include the publish time");
        String width = computed(overlay, "width");
        Assert.assertTrue(width.endsWith("px") && !"0px".equals(width), "Overlay width should be fixed");
        String transition = computed(overlay, "transition");
        Assert.assertTrue("none".equals(transition) || transition.contains("0s"),
                "Slot overlay should appear without a transition");
        boolean ellipsis = "ellipsis".equals(computed(overlay, "text-overflow"));
        if (!ellipsis) {
            for (WebElement line : overlay.findElements(By.xpath(".//*"))) {
                if ("ellipsis".equals(computed(line, "text-overflow"))) {
                    ellipsis = true;
                    break;
                }
            }
        }
        Assert.assertTrue(ellipsis, "Slot excerpts should use ellipsis");
    }

    // ---------------------------------------------------------------------
    // 2.6.10 Publishing
    // ---------------------------------------------------------------------

    @Test(priority = 120, description = "2.6.10 current author publish is highlighted and expanded like to-draft")
    public void publishing_currentAuthorHighlightedExpanded() {
        String token = publishLong();
        Assert.assertTrue(contentShows(token, END_MARKER), "Own published shot should be expanded");
        assertHighlightedThenFades(token);
    }

    @Test(priority = 121, description = "2.6.10 a colleague publish is not expanded")
    public void publishing_otherAuthorNotExpanded() throws Exception {
        final String[] token = new String[1];
        withColleague(new Work() {
            public void run() {
                token[0] = publishLong();
            }
        });
        waitForShot(token[0], publishedName());
        Assert.assertTrue(isClipped(contentOf(token[0])) || !contentShows(token[0], END_MARKER),
                "A shot published by another author should not be expanded");
    }

    @Test(priority = 122, description = "2.6.10.1 publish snackbar links to the shot and stays below the appbar")
    public void publishSnackbar_linkOpensShotBelowAppbar() {
        String token = publishNewShot(false);
        WebElement snackbar = snackbar();
        assertBelowAppbar(snackbar);
        snackbar.findElement(By.xpath(".//a|.//button")).click();
        assertPreviewTab(token);
    }

    @Test(priority = 123, description = "2.6.10.2 snackbar tells the user a colleague just published")
    public void publishSnackbarColleague_textAndFrontLink() throws Exception {
        final String[] token = new String[1];
        withColleague(new Work() {
            public void run() {
                token[0] = publishNewShot(false);
            }
        });
        WebElement snackbar = new WebDriverWait(driver, Duration.ofSeconds(25)).until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(.,'acaba de publicar')]")));
        Assert.assertTrue(snackbar.getText().contains("acaba de publicar."),
                "Colleague snackbar should say the author just published");
        Assert.assertTrue(snackbar.getText().contains("Ver en el superevento"),
                "Colleague snackbar should link to the superevento");
        snackbar.findElement(By.xpath(".//a[contains(.,'superevento')]|.//button[contains(.,'superevento')]")).click();
        assertPreviewTab(token[0]);
    }

    // ---------------------------------------------------------------------
    // 2.6.11 Search
    // ---------------------------------------------------------------------

    @Test(priority = 130, description = "2.6.11 search opens every group and lists only matching shots")
    public void search_headingUncollapsesAndFiltersGroups() {
        try {
            String keep = publishNewShot(false);
            String match = publishNewShot(false);
            setGroupOpen(publishedName(), false);
            setGroupOpen(scheduledName(), false);
            setGroupOpen(draftsName(), false);
            performSearch(match);
            Assert.assertTrue(isGroupOpen(publishedName()) && isGroupOpen(scheduledName()) && isGroupOpen(draftsName()),
                    "Search should uncollapse every render group");
            WebElement heading = searchHeading(match);
            Assert.assertTrue(heading.getText().matches("(?s).*\\d+.*"), "Search heading should include the result count");
            Assert.assertTrue(groupHeader(publishedName()).getText().matches("(?s).*\\d+.*"),
                    "Each group header should show its own result count");
            Assert.assertTrue(groupContains(publishedName(), match), "Matching shots should stay visible");
            Assert.assertFalse(groupContains(publishedName(), keep), "Shots that do not match should be hidden");
            WebElement dismiss = heading.findElement(By.xpath(".//a|.//button"));
            dismiss.click();
            setGroupOpen(publishedName(), true);
            Assert.assertTrue(groupContains(publishedName(), keep), "The heading link should restore the full listing");
        } finally {
            restoreGroupDefaults();
        }
    }

    @Test(priority = 131, description = "2.6.11 the appbar clear control dismisses search")
    public void search_dismissFromAppbar() {
        try {
            String keep = publishNewShot(false);
            String match = publishNewShot(false);
            performSearch(match);
            WebElement input = searchInput();
            WebElement clear = input.findElement(By.xpath(
                    "./ancestor::form[1]//button | ./ancestor::*[contains(@class,'search') or contains(@class,'Search')][1]//button"));
            clear.click();
            setGroupOpen(publishedName(), true);
            Assert.assertTrue(groupContains(publishedName(), keep) && groupContains(publishedName(), match),
                    "Clearing the appbar search should restore every shot");
        } finally {
            restoreGroupDefaults();
        }
    }

    // ---------------------------------------------------------------------
    // Navigation
    // ---------------------------------------------------------------------

    private String loginUrl() {
        String url = textProp("shot_login_url");
        if (!url.isEmpty()) {
            return url;
        }
        return prop.getProperty("admin_login_url", "https://testadmin.xataka.com/");
    }

    private void openActive() {
        if (activeEmisionUrl == null) {
            activeEmisionUrl = resolveActiveUrl();
        }
        driver.get(activeEmisionUrl);
        ensureLoggedIn();
        clickEmisionTabIfNeeded();
        wait.until(d -> isShotFormPresent());
    }

    private String resolveActiveUrl() {
        String configured = textProp("shot_active_emision_url");
        if (!configured.isEmpty()) {
            driver.get(configured);
            ensureLoggedIn();
            clickEmisionTabIfNeeded();
            return driver.getCurrentUrl();
        }
        String create = textProp("shot_create_url");
        if (create.isEmpty()) {
            create = "https://testadmin.xataka.com/post/new?type=superevent";
        }
        driver.get(create);
        ensureLoggedIn();
        clickEmisionTabIfNeeded();
        if (isShotFormPresent()) {
            return driver.getCurrentUrl();
        }
        prepareSupereventoShell();
        clickEmisionTabIfNeeded();
        if (isShotFormPresent()) {
            return driver.getCurrentUrl();
        }
        Assert.fail("Set shot_active_emision_url to the Emisión tab of a superevento in active follow-up");
        return null;
    }

    private void openOver() {
        String url = textProp("shot_over_emision_url");
        Assert.assertFalse(url.isEmpty(),
                "Set shot_over_emision_url to a superevento that is no longer in active follow-up");
        driver.get(url);
        ensureLoggedIn();
        clickEmisionTabIfNeeded();
    }

    private void prepareSupereventoShell() {
        if (driver.findElements(By.xpath(prop.getProperty("post_title"))).isEmpty()) {
            return;
        }
        WebElement title = driver.findElement(By.xpath(prop.getProperty("post_title")));
        title.click();
        title.sendKeys("CreateShot " + System.currentTimeMillis());
        if (!driver.findElements(By.xpath(prop.getProperty("post_content"))).isEmpty()) {
            driver.findElement(By.xpath(prop.getProperty("post_content"))).sendKeys("Superevento para crear shots");
        }
        if (!driver.findElements(By.xpath(prop.getProperty("Auto_save"))).isEmpty()) {
            driver.findElement(By.xpath(prop.getProperty("Auto_save"))).click();
        }
    }

    private void blankSupereventoTitle() {
        List<WebElement> tabs = driver.findElements(By.xpath(
                "//*[@role='tab' or self::a or self::button][contains(normalize-space(.),'Escribir')]"));
        if (!tabs.isEmpty() && tabs.get(0).isDisplayed()) {
            tabs.get(0).click();
        }
        if (driver.findElements(By.xpath(prop.getProperty("post_title"))).isEmpty()) {
            return;
        }
        setInput(driver.findElement(By.xpath(prop.getProperty("post_title"))), "");
        if (!driver.findElements(By.xpath(prop.getProperty("Auto_save"))).isEmpty()) {
            driver.findElement(By.xpath(prop.getProperty("Auto_save"))).click();
        }
    }

    private String readSupereventoTitleFromEditor() {
        try {
            List<WebElement> tabs = driver.findElements(By.xpath(
                    "//*[@role='tab' or self::a or self::button][contains(normalize-space(.),'Escribir')]"));
            if (tabs.isEmpty()) {
                return "";
            }
            tabs.get(0).click();
            WebElement field = new WebDriverWait(driver, Duration.ofSeconds(8)).until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath(prop.getProperty("post_title"))));
            String value = field.getAttribute("value");
            clickEmisionTabIfNeeded();
            wait.until(d -> isShotFormPresent());
            return value == null ? "" : value.trim();
        } catch (Exception e) {
            return "";
        }
    }

    private void clickEmisionTabIfNeeded() {
        if (isShotFormPresent()) {
            return;
        }
        List<WebElement> tabs = driver.findElements(By.xpath(
                "//*[@role='tab' or self::a or self::button][contains(normalize-space(.),'Emisión') or contains(normalize-space(.),'Emision')]"));
        for (WebElement tab : tabs) {
            if (tab.isDisplayed()) {
                tab.click();
                return;
            }
        }
    }

    private void ensureLoggedIn() {
        ensureLoggedInAs(prop.getProperty("admin_usename"), prop.getProperty("admin_pwd"));
    }

    private void ensureLoggedInAs(String user, String password) {
        By usernameBy = By.xpath(prop.getProperty("login_username_txt"));
        if (driver.findElements(usernameBy).isEmpty()) {
            return;
        }
        WebElement username = newWait(driver).until(ExpectedConditions.visibilityOfElementLocated(usernameBy));
        username.clear();
        username.sendKeys(user);
        WebElement pwd = driver.findElement(By.xpath(prop.getProperty("login_pwd_txt")));
        pwd.clear();
        pwd.sendKeys(password);
        driver.findElement(By.xpath(prop.getProperty("login_submit_button"))).click();
        newWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(usernameBy));
    }

    private void withColleague(Work work) throws Exception {
        WebDriver originalDriver = driver;
        WebDriverWait originalWait = wait;
        JavascriptExecutor originalJs = js;
        WebDriver other = colleagueDriver();
        try {
            driver = other;
            js = (JavascriptExecutor) other;
            wait = newWait(other);
            other.get(activeEmisionUrl);
            clickEmisionTabIfNeeded();
            wait.until(d -> isShotFormPresent());
            work.run();
        } finally {
            driver = originalDriver;
            wait = originalWait;
            js = originalJs;
        }
    }

    private WebDriver colleagueDriver() {
        if (colleague != null) {
            return colleague;
        }
        String user = textProp("shot_colleague_user");
        String password = textProp("shot_colleague_pwd");
        Assert.assertFalse(user.isEmpty() || password.isEmpty(), "Set shot_colleague_user and shot_colleague_pwd");
        try {
            Adminproperty otherAdmin = new Adminproperty();
            otherAdmin.ReadProperties();
            colleague = otherAdmin.callproperty(loginUrl(), prop.getProperty("browser", "chrome"));
            colleague.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            colleague.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(60));
            WebDriver current = driver;
            driver = colleague;
            try {
                ensureLoggedInAs(user, password);
            } finally {
                driver = current;
            }
            return colleague;
        } catch (Exception e) {
            throw new IllegalStateException("Colleague login failed", e);
        }
    }

    // ---------------------------------------------------------------------
    // Shot form actions
    // ---------------------------------------------------------------------

    private boolean isShotFormPresent() {
        return !driver.findElements(tip(TIP_PUBLISH)).isEmpty() && !driver.findElements(tip(TIP_CLEAR)).isEmpty();
    }

    private WebElement shotForm() {
        WebElement anchor = displayed(tip(TIP_CLEAR));
        return (WebElement) js.executeScript(
                "let n = arguments[0];"
                        + "while (n && n !== document.body) {"
                        + "  const pos = getComputedStyle(n).position;"
                        + "  const cls = (n.className || '').toString().toLowerCase();"
                        + "  if (pos !== 'fixed' && (n.tagName === 'FORM' || cls.indexOf('shot') >= 0 || cls.indexOf('editor') >= 0 || cls.indexOf('form') >= 0)) return n;"
                        + "  n = n.parentElement;"
                        + "}"
                        + "n = arguments[0];"
                        + "for (let i = 0; i < 6 && n.parentElement; i++) n = n.parentElement;"
                        + "return n;",
                anchor);
    }

    private WebElement editor() {
        WebElement form = shotForm();
        List<WebElement> editors = form.findElements(By.cssSelector("[contenteditable='true'], textarea"));
        for (WebElement editor : editors) {
            if (editor.isDisplayed()) {
                return editor;
            }
        }
        throw new NoSuchElementException("Shot form editor");
    }

    private String formText() {
        try {
            return editor().getText();
        } catch (Exception e) {
            return "";
        }
    }

    private boolean isFormEmpty() {
        WebElement editor = editor();
        return editor.getText().trim().isEmpty() && editor.findElements(By.cssSelector("img")).isEmpty();
    }

    private String fillToken() {
        return fillUnique("contenido de prueba");
    }

    private String fillLong() {
        return fillUnique(repeat("texto de shot ", 40) + END_MARKER);
    }

    private String fillUnique(String body) {
        String token = "s" + System.currentTimeMillis();
        WebElement editor = editor();
        js.executeScript("arguments[0].focus();", editor);
        editor.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        editor.sendKeys(Keys.DELETE);
        editor.sendKeys(token + " " + body);
        return token;
    }

    private void makeFormTall() {
        WebElement editor = editor();
        editor.click();
        for (int i = 0; i < 12; i++) {
            editor.sendKeys("linea " + i);
            editor.sendKeys(Keys.ENTER);
        }
    }

    private String saveDraft() {
        String token = fillToken();
        clickTip(TIP_DRAFT);
        waitForShot(token, draftsName());
        return token;
    }

    private String saveDraftLong() {
        String token = fillLong();
        clickTip(TIP_DRAFT);
        waitForShot(token, draftsName());
        return token;
    }

    private String publishNewShot(boolean longBody) {
        String token = longBody ? fillLong() : fillToken();
        clickTip(TIP_PUBLISH);
        waitForShot(token, publishedName());
        return token;
    }

    private String publishLong() {
        return publishNewShot(true);
    }

    private String scheduleNewShot() {
        String token = fillToken();
        openSchedule();
        confirmSchedule();
        waitForShot(token, scheduledName());
        return token;
    }

    private String scheduleLong() {
        String token = fillLong();
        openSchedule();
        confirmSchedule();
        waitForShot(token, scheduledName());
        return token;
    }

    private String scheduleAt(String time) {
        String token = fillToken();
        openSchedule();
        setInput(scheduleTimeInput(), time);
        confirmSchedule();
        waitForShot(token, scheduledName());
        return token;
    }

    private void openSchedule() {
        if (!scheduleFormVisible()) {
            clickTip(TIP_SCHEDULE);
        }
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(d -> scheduleFormVisible());
    }

    private boolean scheduleFormVisible() {
        try {
            return scheduleTimeInput().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void confirmSchedule() {
        WebElement panel = schedulePanel();
        List<WebElement> buttons = panel.findElements(By.xpath(".//button"));
        for (WebElement button : buttons) {
            if (!button.isDisplayed()) {
                continue;
            }
            String label = labelOf(button).toLowerCase(Locale.ROOT);
            if (label.contains("cancel") || label.contains("hueco")) {
                continue;
            }
            if (label.contains("program") || label.contains("acept") || label.contains("guard") || label.contains("confirm")) {
                button.click();
                return;
            }
        }
        clickTip(TIP_SCHEDULE);
    }

    private WebElement schedulePanel() {
        WebElement time = scheduleTimeInput();
        return (WebElement) js.executeScript(
                "let n = arguments[0];"
                        + "while (n && n.parentElement) {"
                        + "  if (n.querySelectorAll('input, button').length >= 3) return n;"
                        + "  n = n.parentElement;"
                        + "}"
                        + "return arguments[0].parentElement;",
                time);
    }

    private WebElement scheduleTimeInput() {
        String custom = textProp("shot_time_input");
        if (!custom.isEmpty()) {
            return driver.findElement(By.xpath(custom));
        }
        List<WebElement> inputs = driver.findElements(By.xpath("//input[not(@type='hidden') and not(@type='checkbox')]"));
        for (WebElement input : inputs) {
            if (!input.isDisplayed()) {
                continue;
            }
            String blob = blob(input);
            if (blob.contains("hora") || blob.contains("time") || blob.contains("hh")) {
                return input;
            }
        }
        for (WebElement input : inputs) {
            String value = String.valueOf(input.getAttribute("value"));
            if (input.isDisplayed() && value.matches(".*\\d{1,2}:\\d{2}.*")) {
                return input;
            }
        }
        throw new NoSuchElementException("Schedule time input");
    }

    private WebElement scheduleDateInput() {
        WebElement time = scheduleTimeInput();
        List<WebElement> inputs = driver.findElements(By.xpath("//input[not(@type='hidden') and not(@type='checkbox')]"));
        for (WebElement input : inputs) {
            if (!input.isDisplayed() || input.equals(time)) {
                continue;
            }
            String blob = blob(input);
            if (blob.contains("fecha") || blob.contains("date") || blob.contains("dd")) {
                return input;
            }
        }
        for (WebElement input : inputs) {
            if (input.isDisplayed() && !input.equals(time)) {
                return input;
            }
        }
        throw new NoSuchElementException("Schedule date input");
    }

    private WebElement openSlots() {
        openSchedule();
        if (driver.findElements(By.cssSelector("[role='dialog']")).isEmpty()
                || !slotsDialogOrNull().isDisplayed()) {
            clickTip(TIP_PICK_SLOT);
        }
        return new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            WebElement dialog = slotsDialogOrNull();
            return dialog != null && dialog.isDisplayed() ? dialog : null;
        });
    }

    private WebElement slotsDialogOrNull() {
        List<WebElement> dialogs = driver.findElements(By.cssSelector("[role='dialog']"));
        for (WebElement dialog : dialogs) {
            if (dialog.isDisplayed() && (dialog.getText().contains("Ir a hoy") || !dialog.findElements(tipRelative(TIP_TODAY)).isEmpty())) {
                return dialog;
            }
        }
        return dialogs.isEmpty() ? null : dialogs.get(0);
    }

    private void addImage() throws Exception {
        WebElement editor = editor();
        int before = editor.findElements(By.cssSelector("img")).size();
        try {
            clickAsset("Imagen", "Añadir imagen", "Foto", "Image");
        } catch (NoSuchElementException e) {
            openAddMenuIfNeeded();
            clickAsset("Imagen", "Añadir imagen", "Foto", "Image");
        }
        List<WebElement> inputs = driver.findElements(By.cssSelector("input[type='file']"));
        if (!inputs.isEmpty()) {
            inputs.get(inputs.size() - 1).sendKeys(pngFile.getAbsolutePath());
        } else {
            dropFile(editor);
        }
        clickInsertIfPresent();
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(
                d -> {
                    try {
                        return editor().findElements(By.cssSelector("img")).size() > before;
                    } catch (Exception ex) {
                        return false;
                    }
                });
    }

    private void clickAsset(String... labels) {
        for (String label : labels) {
            List<WebElement> buttons = shotForm().findElements(By.xpath(
                    ".//button[contains(@aria-label, " + q(label) + ") or contains(@title, " + q(label)
                            + ") or contains(normalize-space(.), " + q(label) + ")]"));
            for (WebElement button : buttons) {
                if (button.isDisplayed()) {
                    button.click();
                    return;
                }
            }
        }
        throw new NoSuchElementException("Asset button");
    }

    private void clickInsertIfPresent() {
        for (String label : new String[] { "Insertar", "Añadir", "Aceptar", "Guardar", "Subir" }) {
            List<WebElement> buttons = driver.findElements(By.xpath(
                    "//*[@role='dialog']//button[contains(normalize-space(.), " + q(label) + ")]"));
            for (WebElement button : buttons) {
                if (button.isDisplayed()) {
                    button.click();
                    return;
                }
            }
        }
    }

    private void openAddMenuIfNeeded() {
        List<WebElement> buttons = shotForm().findElements(By.tagName("button"));
        for (WebElement button : buttons) {
            if (!button.isDisplayed() || isKnownAction(button)) {
                continue;
            }
            button.click();
            if (assetButtonVisible()) {
                return;
            }
        }
    }

    private boolean assetButtonVisible() {
        for (String label : new String[] { "Imagen", "Añadir imagen", "Foto", "Image" }) {
            if (!shotForm().findElements(By.xpath(
                    ".//button[contains(@aria-label, " + q(label) + ") or contains(@title, " + q(label) + ")]")).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    private WebElement newlyShownButton(int before) {
        List<WebElement> buttons = shotForm().findElements(By.tagName("button"));
        if (buttons.size() <= before) {
            return null;
        }
        for (WebElement button : buttons) {
            if (button.isDisplayed() && !isKnownAction(button)) {
                return button;
            }
        }
        return null;
    }

    private boolean isKnownAction(WebElement button) {
        String label = labelOf(button);
        return label.contains(TIP_PUBLISH) || label.contains(TIP_SCHEDULE) || label.contains(TIP_CLEAR)
                || label.contains(TIP_DRAFT) || label.contains(TIP_PREVIEW) || label.contains(TIP_EDIT)
                || label.contains(TIP_MORE);
    }

    private void dropFile(WebElement target) {
        js.executeScript(
                "const binary = atob(arguments[0]);"
                        + "const bytes = new Uint8Array(binary.length);"
                        + "for (let i = 0; i < binary.length; i++) bytes[i] = binary.charCodeAt(i);"
                        + "const file = new File([bytes], 'shot.png', {type:'image/png'});"
                        + "const dt = new DataTransfer(); dt.items.add(file);"
                        + "const el = arguments[1];"
                        + "['dragenter','dragover','drop'].forEach(type => {"
                        + "  el.dispatchEvent(new DragEvent(type, {bubbles:true, cancelable:true, dataTransfer: dt}));"
                        + "});",
                PNG_1X1, target);
    }

    private WebElement sizeButton(String token) {
        WebElement button = shotForm().findElement(By.xpath(
                ".//button[contains(translate(@aria-label,'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚ','abcdefghijklmnopqrstuvwxyzáéíóú'), "
                        + q(token) + ") or contains(translate(@title,'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚ','abcdefghijklmnopqrstuvwxyzáéíóú'), "
                        + q(token) + ") or contains(translate(normalize-space(.),'ABCDEFGHIJKLMNOPQRSTUVWXYZÁÉÍÓÚ','abcdefghijklmnopqrstuvwxyzáéíóú'), "
                        + q(token) + ")]"));
        return button;
    }

    private WebElement findHelp() {
        List<WebElement> blocks = driver.findElements(By.xpath(
                "//*[contains(translate(., 'ATAJOS', 'atajos'), 'atajo') and string-length(normalize-space(.)) > 8]"));
        for (WebElement block : blocks) {
            if (block.isDisplayed()) {
                return block;
            }
        }
        List<WebElement> buttons = driver.findElements(By.xpath(
                "//button[contains(translate(@aria-label,'ATAJOSAYUDA','atajosayuda'),'atajo')"
                        + " or contains(translate(@title,'ATAJOSAYUDA','atajosayuda'),'atajo')"
                        + " or contains(translate(@aria-label,'AYUDA','ayuda'),'ayuda')]"));
        if (!buttons.isEmpty()) {
            buttons.get(0).click();
        }
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//*[contains(translate(.,'ATAJOS','atajos'),'atajo') or contains(.,'Ctrl')]")));
    }

    // ---------------------------------------------------------------------
    // Render shots and groups
    // ---------------------------------------------------------------------

    private void waitForShot(String token, String group) {
        setGroupOpen(group, true);
        wait.until(d -> {
            try {
                return shotCardNow(token).isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });
    }

    private WebElement shotCard(String token) {
        return wait.until(d -> {
            try {
                return shotCardNow(token);
            } catch (Exception e) {
                return null;
            }
        });
    }

    private WebElement shotCardNow(String token) {
        List<WebElement> nodes = driver.findElements(By.xpath("//*[contains(., " + q(token) + ")]"));
        WebElement best = null;
        int bestLength = Integer.MAX_VALUE;
        for (WebElement node : nodes) {
            String text = node.getText();
            if (text == null || !text.contains(token)) {
                continue;
            }
            if (!(text.contains("Borrador") || text.contains("Programado") || text.contains("Publicado"))) {
                continue;
            }
            if (text.length() < bestLength) {
                best = node;
                bestLength = text.length();
            }
        }
        if (best == null) {
            throw new NoSuchElementException(token);
        }
        return best;
    }

    private WebElement statusOf(String token) {
        WebElement card = shotCard(token);
        WebElement best = card;
        int bestLength = card.getText().length();
        for (WebElement node : card.findElements(By.xpath(".//*"))) {
            String text = node.getText() == null ? "" : node.getText().trim();
            if (!(text.startsWith("Borrador") || text.startsWith("Programado") || text.startsWith("Publicado"))) {
                continue;
            }
            if (text.length() < bestLength) {
                best = node;
                bestLength = text.length();
            }
        }
        return best;
    }

    private WebElement contentOf(String token) {
        WebElement card = shotCard(token);
        WebElement best = card;
        for (WebElement node : card.findElements(By.xpath(".//*[contains(., " + q(token) + ")]"))) {
            String overflow = computed(node, "overflow-y");
            if ("hidden".equals(overflow) || "auto".equals(overflow) || "scroll".equals(overflow) || "clip".equals(overflow)) {
                best = node;
            }
        }
        return best;
    }

    private boolean contentShows(String token, String marker) {
        try {
            return contentOf(token).getText().contains(marker);
        } catch (Exception e) {
            return false;
        }
    }

    private void assertStatusPrefix(String token, String... prefixes) {
        String text = statusOf(token).getText().replace('\u2013', '\u2014').trim();
        boolean matches = false;
        for (String prefix : prefixes) {
            if (text.startsWith(prefix)) {
                matches = true;
            }
        }
        Assert.assertTrue(matches, "Status [" + text + "] should start with " + prefixes[0]);
        Assert.assertTrue(text.matches(".*\\d.*"), "Status should include a datetime: " + text);
    }

    private void assertEllipsisFixedHeight(WebElement element) {
        Assert.assertEquals(computed(element, "text-overflow"), "ellipsis", "Status overflow should be ellipsis");
        String height = computed(element, "height");
        Assert.assertFalse("auto".equals(height) || "0px".equals(height), "Status should use a fixed height");
    }

    private void assertPendingChange(String token, String stillInGroupWord) {
        String beforeText = statusOf(token).getText();
        String beforePaint = paint(statusOf(token));
        clickTipWithin(shotCard(token), TIP_EDIT);
        editor().sendKeys(" cambio pendiente");
        editor().sendKeys(Keys.TAB);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> {
            try {
                return !statusOf(token).getText().equals(beforeText) || !paint(statusOf(token)).equals(beforePaint);
            } catch (Exception e) {
                return false;
            }
        });
        Assert.assertNotEquals(statusOf(token).getText(), beforeText, "Pending changes should change the status text");
        Assert.assertNotEquals(paint(statusOf(token)), beforePaint, "Pending changes should change the status color");
        Assert.assertTrue(shotCard(token).getText().contains(stillInGroupWord),
                "Pending changes should keep the shot in its publishing state");
    }

    private void assertHighlightedThenFades(final String token) {
        String duration = computed(statusOf(token), "transition-duration") + " "
                + computed(statusOf(token).findElement(By.xpath("..")), "transition-duration");
        Assert.assertTrue(duration.contains("2s") || duration.contains("2000"),
                "Highlight should fade for 2s but transition was " + duration);
        final String initial = paint(statusOf(token));
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(d -> !paint(statusOf(token)).equals(initial));
            Assert.fail("Highlighted status should stay for 5 seconds before it fades");
        } catch (TimeoutException stillHighlighted) {
            // Color is still the highlighted one inside the first few seconds.
        }
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(d -> !paint(statusOf(token)).equals(initial));
    }

    private WebElement expandButton(String token) {
        WebElement card = shotCard(token);
        List<WebElement> buttons = card.findElements(By.tagName("button"));
        for (WebElement button : buttons) {
            String label = labelOf(button).toLowerCase(Locale.ROOT);
            if (label.contains("expand") || label.contains("despleg") || label.contains("ver más") || label.contains("ver mas")) {
                return button;
            }
        }
        for (WebElement button : buttons) {
            String label = labelOf(button).toLowerCase(Locale.ROOT);
            if (label.contains("vista previa") || label.contains("editar") || label.contains("más") || label.contains("mas")) {
                continue;
            }
            if (button.isDisplayed()) {
                return button;
            }
        }
        throw new NoSuchElementException("button-expand");
    }

    private boolean isClipped(WebElement content) {
        Object result = js.executeScript("return arguments[0].scrollHeight > arguments[0].clientHeight + 4;", content);
        return Boolean.TRUE.equals(result);
    }

    private void assertNotActionable(WebElement content) {
        if ("none".equals(computed(content, "pointer-events"))) {
            return;
        }
        for (WebElement link : content.findElements(By.tagName("a"))) {
            Assert.assertEquals(computed(link, "pointer-events"), "none",
                    "Links inside a render-shot should not be actionable");
        }
    }

    private void assertOutsideVisibleBox(WebElement content, String marker) {
        List<WebElement> markers = content.findElements(By.xpath(".//*[contains(., " + q(marker) + ")]"));
        if (markers.isEmpty()) {
            return;
        }
        WebElement mark = markers.get(markers.size() - 1);
        int visibleBottom = content.getLocation().getY() + content.getSize().getHeight();
        Assert.assertTrue(mark.getLocation().getY() >= visibleBottom - 2,
                "Only the first asset should be visible in the collapsed card");
    }

    private void assertPatternAndGradient(WebElement content) {
        String images = (String) js.executeScript(
                "function bg(node, pseudo) {"
                        + "  const style = pseudo ? getComputedStyle(node, pseudo) : getComputedStyle(node);"
                        + "  return (style.backgroundImage || '') + ' ' + (style.background || '');"
                        + "}"
                        + "const el = arguments[0];"
                        + "return bg(el) + ' ' + bg(el, '::before') + ' ' + bg(el, '::after');",
                content);
        Assert.assertTrue(images.contains("gradient"), "Content background should include a gradient");
        boolean pattern = images.contains("url(") || images.contains("repeating-")
                || images.indexOf("gradient") != images.lastIndexOf("gradient");
        Assert.assertTrue(pattern, "Content background should include a pattern");
    }

    private WebElement authorAvatar(String token) {
        WebElement card = shotCard(token);
        WebElement content = contentOf(token);
        for (WebElement image : card.findElements(By.tagName("img"))) {
            Boolean inside = (Boolean) js.executeScript("return arguments[0].contains(arguments[1]);", content, image);
            if (!Boolean.TRUE.equals(inside)) {
                return image;
            }
        }
        throw new NoSuchElementException("Author avatar");
    }

    private void openMenu(String token) {
        clickTipWithin(shotCard(token), TIP_MORE);
        new WebDriverWait(driver, Duration.ofSeconds(5)).until(d -> menuContainerVisible());
    }

    private boolean menuContainerVisible() {
        List<WebElement> menus = driver.findElements(By.cssSelector("[role='menu'], [role='listbox']"));
        for (WebElement menu : menus) {
            if (menu.isDisplayed()) {
                return true;
            }
        }
        return false;
    }

    private void closeMenu() {
        driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
    }

    private WebElement menuItem(String... labels) {
        for (String label : labels) {
            List<WebElement> items = driver.findElements(By.xpath(
                    "//*[@role='menu' or @role='listbox']//*[contains(normalize-space(.), " + q(label) + ")]"));
            for (WebElement item : items) {
                if (item.isDisplayed()) {
                    return item;
                }
            }
        }
        return null;
    }

    private String[] toDraftLabels() {
        return new String[] {
                textPropDefault("shot_menu_to_draft", "Enviar a borrador"),
                "Enviar a borrador", "Pasar a borrador", "Mover a borrador", "A borrador"
        };
    }

    private String[] deleteLabels() {
        return new String[] {
                textPropDefault("shot_menu_delete", "Eliminar"),
                "Eliminar", "Borrar shot", "Suprimir"
        };
    }

    private void confirmIfDialog() {
        List<WebElement> buttons = driver.findElements(By.xpath(
                "//*[@role='dialog']//button[contains(.,'Aceptar') or contains(.,'Confirmar') or contains(.,'Eliminar')"
                        + " or contains(.,'Enviar') or contains(.,'Borrar')]"));
        for (WebElement button : buttons) {
            if (button.isDisplayed()) {
                button.click();
                return;
            }
        }
    }

    private String publishedName() {
        return textPropDefault("shot_group_published", "Publicados");
    }

    private String scheduledName() {
        return textPropDefault("shot_group_scheduled", "Programados");
    }

    private String draftsName() {
        return textPropDefault("shot_group_drafts", "Borradores");
    }

    private WebElement groupHeader(String name) {
        By by = By.xpath(
                "//*[self::button or self::h2 or self::h3 or self::h4 or @role='button' or self::summary]"
                        + "[contains(normalize-space(.), " + q(name) + ")"
                        + " and not(.//*[self::button or self::h2 or self::h3 or self::h4 or @role='button'][contains(normalize-space(.), "
                        + q(name) + ")])]");
        return new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    private WebElement groupPanel(String name) {
        WebElement header = groupHeader(name);
        WebElement row = (WebElement) js.executeScript(
                "let n = arguments[0];"
                        + "while (n.parentElement && n.parentElement.children.length <= 2) n = n.parentElement;"
                        + "return n;",
                header);
        List<WebElement> siblings = row.findElements(By.xpath("./following-sibling::*"));
        if (!siblings.isEmpty()) {
            return siblings.get(0);
        }
        return row;
    }

    private WebElement groupSection(String name) {
        WebElement header = groupHeader(name);
        return (WebElement) js.executeScript(
                "return arguments[0].closest('section') || arguments[0].parentElement;", header);
    }

    private boolean isGroupOpen(String name) {
        WebElement header = groupHeader(name);
        String expanded = header.getAttribute("aria-expanded");
        if ("true".equalsIgnoreCase(expanded)) {
            return true;
        }
        if ("false".equalsIgnoreCase(expanded)) {
            return false;
        }
        WebElement panel = groupPanel(name);
        if (!panel.isDisplayed() || "none".equals(computed(panel, "display")) || panel.getAttribute("hidden") != null) {
            return false;
        }
        return panel.getSize().getHeight() > 0;
    }

    private void setGroupOpen(String name, boolean open) {
        if (isGroupOpen(name) == open) {
            return;
        }
        groupHeader(name).click();
        new WebDriverWait(driver, Duration.ofSeconds(8)).until(d -> isGroupOpen(name) == open);
    }

    private void restoreGroupDefaults() {
        try {
            setGroupOpen(publishedName(), true);
            setGroupOpen(scheduledName(), true);
            setGroupOpen(draftsName(), false);
        } catch (Exception ignored) {
            // The next test reloads the page and will report a missing group itself.
        }
    }

    private boolean groupContains(String group, String token) {
        try {
            setGroupOpen(group, true);
            return groupPanel(group).getText().contains(token);
        } catch (Exception e) {
            return false;
        }
    }

    private List<WebElement> shotCards(String group) {
        List<WebElement> cards = new ArrayList<WebElement>();
        WebElement panel = groupPanel(group);
        List<WebElement> statuses = panel.findElements(By.xpath(
                ".//*[starts-with(normalize-space(.), 'Borrador') or starts-with(normalize-space(.), 'Programado')"
                        + " or starts-with(normalize-space(.), 'Publicado')]"));
        for (WebElement status : statuses) {
            if (!status.isDisplayed()) {
                continue;
            }
            WebElement card = (WebElement) js.executeScript(
                    "let n = arguments[0];"
                            + "while (n.parentElement && n.parentElement !== arguments[1]) {"
                            + "  if (n.querySelector('img, button')) return n;"
                            + "  n = n.parentElement;"
                            + "}"
                            + "return n;",
                    status, panel);
            if (!cards.contains(card)) {
                cards.add(card);
            }
        }
        return cards;
    }

    private int shotCount(String group) {
        boolean open = isGroupOpen(group);
        if (!open) {
            setGroupOpen(group, true);
        }
        int count = shotCards(group).size();
        if (!open) {
            setGroupOpen(group, false);
        }
        return count;
    }

    private int totalShots() {
        int total = 0;
        for (String group : new String[] { publishedName(), scheduledName(), draftsName() }) {
            total += shotCount(group);
        }
        return total;
    }

    private int cardY(String token) {
        return shotCard(token).getLocation().getY();
    }

    private List<WebElement> dateDividers(String group) {
        List<WebElement> dividers = new ArrayList<WebElement>();
        for (WebElement node : groupPanel(group).findElements(By.xpath(".//*[self::h2 or self::h3 or self::h4 or self::p or self::time or self::div]"))) {
            String text = node.getText() == null ? "" : node.getText().trim();
            if (text.length() == 0 || text.length() > 40) {
                continue;
            }
            if (text.contains("Publicado") || text.contains("Programado") || text.contains("Borrador")) {
                continue;
            }
            if (text.matches(".*\\d{1,2}[/\\-.]\\d{1,2}[/\\-.]\\d{2,4}.*") && node.isDisplayed()) {
                dividers.add(node);
            }
        }
        return dividers;
    }

    private WebElement toggleButton(String group) {
        WebElement header = groupHeader(group);
        List<WebElement> buttons = header.findElements(By.xpath(".//button"));
        if (!buttons.isEmpty()) {
            return buttons.get(0);
        }
        return header;
    }

    private WebElement goToDateButton(String group) {
        WebElement section = groupSection(group);
        List<WebElement> buttons = section.findElements(By.xpath(
                ".//*[contains(@title, 'Ir al día') or contains(@aria-label, 'Ir al día') or contains(normalize-space(.), 'Ir al día')]"));
        for (WebElement button : buttons) {
            if (button.isDisplayed()) {
                return button;
            }
        }
        return null;
    }

    private WebElement pager(String group, boolean previous) {
        WebElement section = groupSection(group);
        String token = previous ? "nterior" : "iguiente";
        String english = previous ? "prev" : "next";
        for (WebElement button : section.findElements(By.tagName("button"))) {
            if (!button.isDisplayed()) {
                continue;
            }
            String blob = labelOf(button).toLowerCase(Locale.ROOT) + " " + String.valueOf(button.getAttribute("class")).toLowerCase(Locale.ROOT);
            if (blob.contains(token) || blob.contains(english)) {
                return button;
            }
        }
        return null;
    }

    private boolean isDisabled(WebElement element) {
        return element == null || !element.isEnabled()
                || "true".equals(element.getAttribute("aria-disabled"))
                || String.valueOf(element.getAttribute("class")).contains("disabled");
    }

    private By calendarBy() {
        return By.cssSelector(".MuiDateCalendar-root, .MuiPickersLayout-root, .MuiCalendarPicker-root, [role='dialog'] [role='grid']");
    }

    private WebElement calendarPopup() {
        return new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.visibilityOfElementLocated(calendarBy()));
    }

    private void openConfigMenu() {
        if (isTextDisplayed(MENU_EXPANDED)) {
            return;
        }
        String custom = textProp("shot_config_button");
        if (!custom.isEmpty()) {
            displayed(By.xpath(custom)).click();
            wait.until(d -> isTextDisplayed(MENU_EXPANDED));
            return;
        }
        WebElement scope = (WebElement) js.executeScript(
                "let n = arguments[0]; for (let i = 0; i < 5 && n.parentElement; i++) n = n.parentElement; return n;",
                groupHeader(publishedName()));
        List<WebElement> buttons = scope.findElements(By.xpath(".//button[@aria-haspopup='menu' or @aria-haspopup='true']"));
        for (WebElement button : buttons) {
            if (!button.isDisplayed()) {
                continue;
            }
            String label = labelOf(button).toLowerCase(Locale.ROOT);
            if (label.contains("plegar") || label.contains("desplegar") || label.contains("más acciones")) {
                continue;
            }
            button.click();
            if (isTextDisplayed(MENU_EXPANDED)) {
                return;
            }
            driver.findElement(By.tagName("body")).sendKeys(Keys.ESCAPE);
        }
        Assert.fail("Set shot_config_button to the render-groups configuration button");
    }

    private WebElement overStatus() {
        List<WebElement> nodes = driver.findElements(By.cssSelector("[role='status'], [role='alert'], [class*='banner'], [class*='status']"));
        for (WebElement node : nodes) {
            if (!node.isDisplayed()) {
                continue;
            }
            String text = node.getText();
            if (text.contains("Borrador —") || text.contains("Publicado —") || text.contains("Programado —")) {
                continue;
            }
            if (!text.trim().isEmpty()) {
                return node;
            }
        }
        throw new NoSuchElementException("Status message for a superevento that is not in active follow-up");
    }

    private WebElement emisionTitle() {
        String custom = textProp("shot_title");
        if (!custom.isEmpty()) {
            return driver.findElement(By.xpath(custom));
        }
        By[] candidates = new By[] {
                By.cssSelector("[data-testid='superevento-title']"),
                By.xpath("//h1"),
                By.xpath("//*[@role='tabpanel']//h2")
        };
        for (By by : candidates) {
            for (WebElement element : driver.findElements(by)) {
                if (element.isDisplayed() && !element.getText().trim().isEmpty()) {
                    return element;
                }
            }
        }
        throw new NoSuchElementException("Superevento title");
    }

    private String firstColumn(WebElement dialog) {
        List<WebElement> headers = dialog.findElements(By.cssSelector("th, [role='columnheader']"));
        for (WebElement header : headers) {
            if (header.isDisplayed() && !header.getText().trim().isEmpty()) {
                return header.getText().trim();
            }
        }
        String text = dialog.getText().trim();
        return text.substring(0, Math.min(80, text.length()));
    }

    private WebElement busyCell(WebElement dialog, String text) {
        for (WebElement cell : dialog.findElements(By.cssSelector("td, [role='gridcell']"))) {
            if (cell.isDisplayed() && cell.getText().contains(text)) {
                return cell;
            }
        }
        throw new NoSuchElementException("Busy slot containing " + text);
    }

    // ---------------------------------------------------------------------
    // Search, snackbar, preview, viewport
    // ---------------------------------------------------------------------

    private WebElement searchInput() {
        String custom = textProp("shot_search_input");
        if (!custom.isEmpty()) {
            return driver.findElement(By.xpath(custom));
        }
        List<WebElement> inputs = driver.findElements(By.xpath(
                "//header//input | //*[@role='banner']//input | //*[contains(@class,'AppBar') or contains(@class,'appbar')]//input"));
        for (WebElement input : inputs) {
            if (input.isDisplayed()) {
                return input;
            }
        }
        throw new NoSuchElementException("Emisión search field in the appbar");
    }

    private void performSearch(String query) {
        WebElement input = searchInput();
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        input.sendKeys(query);
        input.sendKeys(Keys.ENTER);
        searchHeading(query);
    }

    private WebElement searchHeading(String query) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
                "//*[contains(translate(., 'RESULTADOS', 'resultados'), 'resultado') and contains(., " + q(query) + ")]")));
    }

    private WebElement snackbar() {
        WebElement raw = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".MuiSnackbar-root, [role='alert']")));
        return (WebElement) js.executeScript(
                "let n = arguments[0];"
                        + "while (n) { if (getComputedStyle(n).position === 'fixed') return n; n = n.parentElement; }"
                        + "return arguments[0];",
                raw);
    }

    private void assertBelowAppbar(WebElement bar) {
        Assert.assertEquals(computed(bar, "position"), "fixed", "Snackbar should use a fixed position");
        WebElement appbar = appbar();
        int appbarBottom = appbar.getLocation().getY() + appbar.getSize().getHeight();
        Assert.assertTrue(bar.getLocation().getY() >= appbarBottom - 2,
                "Snackbar should sit below the appbar");
    }

    private void assertPreviewTab(String token) {
        String original = driver.getWindowHandle();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> d.getWindowHandles().size() > 1);
        for (String handle : driver.getWindowHandles()) {
            if (!handle.equals(original)) {
                driver.switchTo().window(handle);
                break;
            }
        }
        new WebDriverWait(driver, Duration.ofSeconds(15)).until(d -> d.getPageSource().contains(token));
        Assert.assertTrue(driver.getPageSource().contains(token), "Preview should show the current shot");
        WebElement mark = driver.findElement(By.xpath("//*[contains(text(), " + q(token) + ")]"));
        Assert.assertTrue(inViewport(mark), "Preview should scroll to the shot");
        Object smooth = ((JavascriptExecutor) driver).executeScript(
                "const nodes = [document.documentElement, document.body];"
                        + "return nodes.some(node => getComputedStyle(node).scrollBehavior === 'smooth');");
        Assert.assertEquals(Boolean.TRUE, smooth, "Preview scroll should be smooth");
    }

    private void assertSmoothScroll(WebElement link) {
        String behavior = (String) js.executeScript("return getComputedStyle(document.documentElement).scrollBehavior;");
        link.click();
        if ("smooth".equals(behavior)) {
            return;
        }
        List<Long> samples = new ArrayList<Long>();
        long end = System.currentTimeMillis() + 1200;
        while (System.currentTimeMillis() < end) {
            Object value = js.executeScript("return Math.round(window.scrollY || document.documentElement.scrollTop || 0);");
            if (value instanceof Long) {
                samples.add((Long) value);
            } else if (value instanceof Number) {
                samples.add(((Number) value).longValue());
            }
            try {
                Thread.sleep(70);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        Assert.assertTrue(new LinkedHashSet<Long>(samples).size() > 2, "Scroll to the draft should animate smoothly");
    }

    private String noticeTexts() {
        StringBuilder builder = new StringBuilder();
        for (WebElement element : driver.findElements(By.cssSelector("[role='alert'], .MuiSnackbar-root"))) {
            if (element.isDisplayed()) {
                builder.append(element.getText()).append('|');
            }
        }
        return builder.toString();
    }

    private WebElement stickyToolbarOrNull() {
        WebElement form = null;
        try {
            form = shotForm();
        } catch (Exception ignored) {
            // The form can be off screen or absent.
        }
        for (WebElement anchor : driver.findElements(tip(TIP_PUBLISH))) {
            if (!anchor.isDisplayed()) {
                continue;
            }
            if (form != null && Boolean.TRUE.equals(js.executeScript("return arguments[0].contains(arguments[1]);", form, anchor))) {
                continue;
            }
            WebElement bar = (WebElement) js.executeScript(
                    "let n = arguments[0];"
                            + "while (n && n.parentElement) {"
                            + "  const position = getComputedStyle(n).position;"
                            + "  if (position === 'fixed' || position === 'sticky') return n;"
                            + "  n = n.parentElement;"
                            + "}"
                            + "return null;",
                    anchor);
            if (bar != null && bar.isDisplayed()) {
                return bar;
            }
        }
        return null;
    }

    private WebElement inFormAction(String tooltip) {
        return shotForm().findElement(tipRelative(tooltip));
    }

    private WebElement newShotFabOrNull() {
        for (WebElement button : driver.findElements(By.cssSelector("button, [role='button']"))) {
            if (!button.isDisplayed() || !"fixed".equals(computed(button, "position"))) {
                continue;
            }
            String label = labelOf(button).toLowerCase(Locale.ROOT);
            if (label.contains("publicar") || label.contains("programar") || label.contains("pegar")
                    || label.contains("vista previa") || label.contains("añadir") || label.contains("anadir")) {
                continue;
            }
            Object lowerHalf = js.executeScript(
                    "const rect = arguments[0].getBoundingClientRect(); return rect.bottom > window.innerHeight * 0.6;", button);
            if (Boolean.TRUE.equals(lowerHalf)) {
                return button;
            }
        }
        return null;
    }

    private boolean pasteFabVisible() {
        return fixedFabContaining("pegar") != null;
    }

    private boolean addAssetFabVisible() {
        return fixedFabContaining("añadir") != null || fixedFabContaining("anadir") != null || fixedFabContaining("asset") != null;
    }

    private WebElement fixedFabContaining(String token) {
        for (WebElement button : driver.findElements(By.cssSelector("button, [role='button']"))) {
            if (button.isDisplayed() && "fixed".equals(computed(button, "position"))
                    && labelOf(button).toLowerCase(Locale.ROOT).contains(token)) {
                return button;
            }
        }
        return null;
    }

    private WebElement appbar() {
        return driver.findElement(By.cssSelector("header, [role='banner'], .MuiAppBar-root"));
    }

    private boolean inViewport(WebElement element) {
        Object value = js.executeScript(
                "const rect = arguments[0].getBoundingClientRect();"
                        + "const height = window.innerHeight || document.documentElement.clientHeight;"
                        + "const width = window.innerWidth || document.documentElement.clientWidth;"
                        + "return rect.width > 0 && rect.height > 0 && rect.bottom > 0 && rect.right > 0 && rect.top < height && rect.left < width;",
                element);
        return Boolean.TRUE.equals(value);
    }

    private void scrollToTop() {
        js.executeScript("window.scrollTo(0, 0);");
    }

    private void scrollFormActionsOut() {
        for (int i = 0; i < 20; i++) {
            if (!inViewport(inFormAction(TIP_PUBLISH)) && inViewport(shotForm())) {
                return;
            }
            js.executeScript("window.scrollBy(0, 180);");
        }
        Assert.fail("Could not scroll the form actions off screen while keeping the form visible");
    }

    private void scrollUntilFormLeaves() {
        for (int i = 0; i < 30; i++) {
            if (!inViewport(shotForm())) {
                return;
            }
            js.executeScript("window.scrollBy(0, 400);");
        }
        Assert.fail("Could not scroll the shot form off screen");
    }

    // ---------------------------------------------------------------------
    // Small utilities
    // ---------------------------------------------------------------------

    private void clickTip(String tooltip) {
        WebElement element = displayed(tip(tooltip));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

    private void clickTipWithin(WebElement scope, String tooltip) {
        WebElement element = scope.findElement(tipRelative(tooltip));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", element);
        element.click();
    }

    private void assertTooltip(WebElement element, String expected) {
        String title = element.getAttribute("title");
        String aria = element.getAttribute("aria-label");
        if (expected.equals(title) || expected.equals(aria)) {
            return;
        }
        new Actions(driver).moveToElement(element).pause(Duration.ofMillis(400)).perform();
        WebElement tip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role='tooltip']")));
        Assert.assertTrue(tip.getText().trim().contains(expected),
                "Tooltip should be [" + expected + "] but was [" + tip.getText().trim() + "]");
    }

    private void assertTooltipContains(WebElement element, String expected) {
        String title = firstNonEmpty(element.getAttribute("title"), element.getAttribute("aria-label"));
        if (title.contains(expected)) {
            return;
        }
        new Actions(driver).moveToElement(element).pause(Duration.ofMillis(400)).perform();
        WebElement tip = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@role='tooltip']")));
        Assert.assertTrue(tip.getText().contains(expected),
                "Tooltip should contain [" + expected + "] but was [" + tip.getText().trim() + "]");
    }

    private void assertError(String message) {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[contains(normalize-space(.), " + q(message) + ")]")));
        Assert.assertTrue(error.isDisplayed(), message);
    }

    private void assertErrorEventually(String message) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3)).until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(normalize-space(.), " + q(message) + ")]")));
        } catch (TimeoutException e) {
            confirmSchedule();
            assertError(message);
        }
    }

    private void setInput(WebElement input, String value) {
        input.click();
        input.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        input.sendKeys(Keys.DELETE);
        if (value != null && !value.isEmpty()) {
            input.sendKeys(value);
        }
    }

    private void leaveField(WebElement input) {
        input.sendKeys(Keys.TAB);
    }

    private WebElement displayed(final By by) {
        return wait.until(d -> {
            for (WebElement element : driver.findElements(by)) {
                try {
                    if (element.isDisplayed()) {
                        return element;
                    }
                } catch (StaleElementReferenceException ignored) {
                    // The next poll will look the element up again.
                }
            }
            return null;
        });
    }

    private boolean isTextDisplayed(String text) {
        for (WebElement element : driver.findElements(By.xpath("//*[contains(normalize-space(.), " + q(text) + ")]"))) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (StaleElementReferenceException ignored) {
                // Ignore a node that was replaced while the menu opened.
            }
        }
        return false;
    }

    private boolean pageHas(String token) {
        return driver.getPageSource().contains(token);
    }

    private By tip(String tooltip) {
        return By.xpath("//*" + tipPredicate(tooltip));
    }

    private By tipRelative(String tooltip) {
        return By.xpath(".//*" + tipPredicate(tooltip));
    }

    private String tipPredicate(String tooltip) {
        String dotted = tooltip.replace("…", "...");
        return "[@title=" + q(tooltip) + " or @aria-label=" + q(tooltip)
                + " or @title=" + q(dotted) + " or @aria-label=" + q(dotted) + "]";
    }

    private static String q(String value) {
        if (value.indexOf('\'') < 0) {
            return "'" + value + "'";
        }
        if (value.indexOf('"') < 0) {
            return "\"" + value + "\"";
        }
        return "concat('" + value.replace("'", "', \"'\", '") + "')";
    }

    private static String labelOf(WebElement element) {
        return firstNonEmpty(element.getAttribute("aria-label"), element.getAttribute("title")) + " " + element.getText();
    }

    private static String blob(WebElement input) {
        return (firstNonEmpty(input.getAttribute("aria-label"), "") + " "
                + firstNonEmpty(input.getAttribute("placeholder"), "") + " "
                + firstNonEmpty(input.getAttribute("name"), "") + " "
                + firstNonEmpty(input.getAttribute("id"), "")).toLowerCase(Locale.ROOT);
    }

    private static String firstNonEmpty(String first, String second) {
        if (first != null && !first.trim().isEmpty()) {
            return first.trim();
        }
        if (second != null && !second.trim().isEmpty()) {
            return second.trim();
        }
        return "";
    }

    private String computed(WebElement element, String property) {
        Object value = js.executeScript(
                "return getComputedStyle(arguments[0]).getPropertyValue(arguments[1]);", element, property);
        return value == null ? "" : value.toString().trim();
    }

    private String paint(WebElement element) {
        return computed(element, "color") + "|" + computed(element, "background-color");
    }

    private String browserTimePlus(int minutes) {
        Object value = js.executeScript(
                "const date = new Date(); date.setMinutes(date.getMinutes() + arguments[0]);"
                        + "const pad = n => ('0' + n).slice(-2);"
                        + "return pad(date.getHours()) + ':' + pad(date.getMinutes());",
                minutes);
        return String.valueOf(value);
    }

    private int minutesOf(String value) {
        Matcher matcher = Pattern.compile("(\\d{1,2}):(\\d{2})").matcher(value == null ? "" : value);
        Assert.assertTrue(matcher.find(), "Expected an hh:mm value but was " + value);
        return Integer.parseInt(matcher.group(1)) * 60 + Integer.parseInt(matcher.group(2));
    }

    private String textProp(String key) {
        String value = prop.getProperty(key, "");
        return value == null ? "" : value.trim();
    }

    private String textPropDefault(String key, String fallback) {
        String value = textProp(key);
        return value.isEmpty() ? fallback : value;
    }

    private WebDriverWait newWait(WebDriver target) {
        WebDriverWait targetWait = new WebDriverWait(target, Duration.ofSeconds(20));
        targetWait.ignoring(StaleElementReferenceException.class);
        return targetWait;
    }

    private static String repeat(String value, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(value);
        }
        return builder.toString();
    }
}
