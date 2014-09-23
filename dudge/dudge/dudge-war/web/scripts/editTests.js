var TestView = function(element) {
    this.panelView = element;
    this.testNameView = element.find(".dudge-test-name");
    this.collapseHeaderView = element.find(".dudge-test-header");
    this.collapseContentView = element.find(".panel-collapse");
    this.caretView = element.find(".caret");
    this.errorView = element.find(".dudge-test-error");
    this.saveView = element.find(".dudge-test-save");
    this.saveTextView = element.find(".dudge-test-save-text");
    this.throbberView = element.find(".dudge-test-throbber");
    this.removeView = element.find(".dudge-test-remove");
    this.inputTestView = element.find(".dudge-test-input");
    this.outputTestView = element.find(".dudge-test-output");
    
    this.panelView.removeClass("hidden");
    
    this.collapseHeaderView.click((function() {
        this.collapseContentView.collapse("toggle");
    }).bind(this));
    this.collapseContentView.on("show.bs.collapse hide.bs.collapse", (function (e) {
        var target = $(e.target);
        target.parent().toggleClass("dropup");
        if (e.type === "show" && typeof(this.onExpand) === "function") {
            this.onExpand(this);
        }
    }).bind(this));
    this.saveView.click((function(){
        if (typeof(this.onSave) === "function") {
            this.onSave(this);
        }
    }).bind(this));
    this.removeView.click((function(){
        if (typeof(this.onRemove) === "function") {
            this.onRemove(this);
        }
    }).bind(this));
    var onInput = (function(){
        if (typeof(this.onEdit) === "function") {
            this.onEdit(this);
        }
    }).bind(this);
    this.inputTestView.on("input", onInput);
    this.outputTestView.on("input", onInput);
};

TestView.prototype = {
    onSave: null,
    onRemove: null,
    onExpand: null,
    onEdit: null,
    getView: function() {
        return this.panelView;
    },
    disableEditing: function() {
        this.inputTestView.prop("disabled", true);
        this.outputTestView.prop("disabled", true);
    },
    enableEditing: function() {
        this.inputTestView.prop("disabled", false);
        this.outputTestView.prop("disabled", false);
    },
    showError: function(error) {
        this.errorView.text(error);
        this.errorView.removeClass("hidden");
    },
    hideError: function() {
        this.errorView.addClass("hidden");
    },
    setEditableText: function(inputText, outputText) {
        if (this.inputTestView.val() !== inputText) {
            this.inputTestView.val(inputText);
        }
        if (this.outputTestView.val() !== outputText) {
            this.outputTestView.val(outputText);
        }
    },
    setButtonText: function(text) {
        this.saveTextView.text(text);
    },
    enableButton: function() {
        this.saveView.prop("disabled", false);
    },
    disableButton: function() {
        this.saveView.prop("disabled", true);
    },
    setTitle: function(title) {
        this.testNameView.text(title);
    },
    showThrobber: function() {
        this.throbberView.removeClass("hidden");
    },
    hideThrobber: function() {
        this.throbberView.addClass("hidden");
    },
    getInputText: function() {
        return this.inputTestView.val();
    },
    getOutputText: function() {
        return this.outputTestView.val();
    }
};

var Test = function() {
    
};

Test.State = {
    Saved: 0,
    NotSaved: 1,
    Saving: 2,
    NotLoaded: 3,
    Loading: 4
};

Test.prototype = {
    index: 0,
    identifier: 0,
    state: Test.State.Saved,
    isError: false,
    inputTest: null,
    outputTest: null
};

var initTests = function(testList, template, l10n) {
    var tests = [];
    var testViews = [];
    
    var updateView = function(testView, test) {
        testView.setTitle(l10n.testText + " " + (test.index + 1));
        testView.setEditableText(test.inputTest, test.outputTest);
        switch (test.state) {
            case Test.State.Saved:
                testView.hideError();
                testView.enableEditing();
                testView.hideThrobber();
                testView.setButtonText(l10n.savedText);
                testView.disableButton();
                break;
            case Test.State.NotSaved:
                testView.hideError();
                testView.enableEditing();
                testView.hideThrobber();
                testView.setButtonText(l10n.saveText);
                testView.enableButton();
                break;
            case Test.State.Saving:
                testView.hideError();
                testView.disableEditing();
                testView.showThrobber();
                testView.setButtonText(l10n.savingText);
                testView.disableButton();
                break;
            case Test.State.NotLoaded:
                testView.disableEditing();
                testView.hideThrobber();
                if (test.isError) {
                    testView.showError(l10n.loadingErrorText);
                    testView.setButtonText(l10n.tryAgainText);
                    testView.enableButton();
                } else {
                    testView.hideError();
                    testView.setButtonText(l10n.savedText);
                    testView.disableButton();
                }
                break;
            case Test.State.Loading:
                testView.disableEditing();
                testView.showThrobber();
                testView.hideError();
                testView.setButtonText(l10n.savedText);
                testView.disableButton();
                break;
        }
    };
    
    var loadTest = function(iTest) {
        var test = tests[iTest];
        var testView = testViews[iTest];
        if (test.state !== Test.State.NotLoaded) {
            return;
        }
        test.state = Test.State.Loading;
        test.isError = false;
        updateView(testView, test);
        
        $.getJSON(
            "problems.do",
            {
                reqCode: "getTest",
                testId: test.identifier
            },
            function(json) {
                test.inputTest = json.inputData;
                test.outputTest = json.outputData;
                test.isError = false;
                test.state = Test.State.Saved;
                updateView(testView, test);
            }
        ).fail(function() {
            test.isError = true;
            test.state = Test.State.NotLoaded;
            updateView(testView, test);
        });
    };
    
    var saveTest = function(iTest) {
        var test = tests[iTest];
        var testView = testViews[iTest];
        if (test.state !== Test.State.NotSaved) {
            return;
        }
        test.state = Test.State.Saving;
        test.isError = false;
        updateView(testView, test);
        
        $.post(
            "problems.do",
            {
                reqCode: "commitTest",
                testId: test.identifier,
                inputData: test.inputTest,
                outputData: test.outputTest
            },
            function() {
                test.isError = false;
                test.state = Test.State.Saved;
                updateView(testView, test);
            }
        ).fail(function() {
            test.isError = true;
            test.state = Test.State.NotSaved;
            updateView(testView, test);
        });
    };
    
    var onSaveTest = function(testView) {
        var iTest = testViews.indexOf(testView);
        if (iTest < 0) {
            return;
        }
        var test = tests[iTest];
        switch (test.state) {
            case Test.State.NotSaved:
                saveTest(iTest);
                break;
            case Test.State.NotLoaded:
                loadTest(iTest);
                break;
        }
    };
    
    var onRemoveTest = function(testView) {
        var iTest = testViews.indexOf(testView);
        if (iTest < 0) {
            return;
        }
    };
    
    var onTestViewExpand = function(testView) {
        var iTest = testViews.indexOf(testView);
        if (iTest < 0) {
            return;
        }
        if (tests[iTest].state !== Test.State.NotLoaded) {
            return;
        }
        loadTest(iTest);
    };
    
    var onTestViewEdit = function(testView) {
        var iTest = testViews.indexOf(testView);
        if (iTest < 0) {
            return;
        }
        var test = tests[iTest];
        test.state = Test.State.NotSaved;
        test.inputTest = testView.getInputText();
        test.outputTest = testView.getOutputText();
        updateView(testView, test);
    };
    
    for (var iTest = 0; iTest < testList.length - 1; iTest++) {
        var test = new Test();
        test.state = Test.State.NotLoaded;
        test.index = iTest;
        test.identifier = testList[iTest].testId;
        tests.push(test);
        
        var testView = new TestView(template.clone());
        template.parent().append(testView.getView());        
        testView.onExpand = onTestViewExpand;
        testView.onEdit = onTestViewEdit;
        testView.onSave = onSaveTest;
        testView.onRemove = onRemoveTest;
        testViews.push(testView);
        updateView(testView, test);
    }
};
