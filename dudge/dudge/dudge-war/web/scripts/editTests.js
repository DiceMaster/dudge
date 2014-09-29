var TestView = function(element) {
    this.panelView = element;
    this.testNameView = element.find(".dudge-test-name");
    this.collapseHeaderView = element.find(".dudge-test-header");
    this.collapseContentView = element.find(".panel-collapse");
    this.caretView = element.find(".caret");
    this.errorView = element.find(".dudge-test-error");
    this.saveLoadView = element.find(".dudge-test-save");
    this.saveLoadTextView = element.find(".dudge-test-save-text");
    this.loadThrobberView = element.find(".dudge-test-load-throbber");
    this.saveThrobberView = element.find(".dudge-test-throbber");
    this.removeThrobberView = element.find(".dudge-test-remove-throbber");
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
    this.saveLoadView.click((function(){
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
    expand: function() {
        this.collapseContentView.collapse("show");
    },
    toggleEditing: function(isEnabled) {
        this.inputTestView.prop("disabled", !isEnabled);
        this.outputTestView.prop("disabled", !isEnabled);
    },
    showError: function(error) {
        this.errorView.text(error);
        this.errorView.removeClass("hidden");
    },
    hideError: function() {
        this.errorView.addClass("hidden");
    },
    getInputText: function() {
        return this.inputTestView.val();
    },
    getOutputText: function() {
        return this.outputTestView.val();
    },
    setEditableText: function(inputText, outputText) {
        if (this.inputTestView.val() !== inputText) {
            this.inputTestView.val(inputText);
        }
        if (this.outputTestView.val() !== outputText) {
            this.outputTestView.val(outputText);
        }
    },
    setSaveLoadButtonText: function(text) {
        this.saveLoadTextView.text(text);
    },
    toggleSaveLoadButton: function(isEnabled) {
        this.saveLoadView.prop("disabled", !isEnabled);
    },
    toggleRemoveButton: function(isEnabled) {
        this.removeView.prop("disabled", !isEnabled);
    },
    setTitle: function(title) {
        this.testNameView.text(title);
    },
    toggleSaveThrobber: function(isVisible) {
        if (isVisible) {
            this.saveThrobberView.removeClass("hidden");
        } else {
            this.saveThrobberView.addClass("hidden");
        }
    },
    toggleLoadThrobber: function(isVisible) {
        if (isVisible) {
            this.loadThrobberView.removeClass("hidden");
        } else {
            this.loadThrobberView.addClass("hidden");
        }
    },
    toggleRemoveThrobber: function(isVisible) {
        if (isVisible) {
            this.removeThrobberView.removeClass("hidden");
        } else {
            this.removeThrobberView.addClass("hidden");            
        }
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

Test.RemoveState = {
    NotRemoving: 0,
    Removing: 1
};

Test.Error = {
    NoError: 0,
    LoadError: 1,
    SaveError: 2,
    RemoveError: 3
};

Test.prototype = {
    index: 0,
    identifier: 0,
    saveLoadState: Test.State.Saved,
    removeState: Test.RemoveState.NotRemoving,
    error: Test.Error.NoError,
    inputTest: null,
    outputTest: null
};

var initTests = function(testList, template, l10n) {
    var tests = [];
    var testViews = [];
    var selectedTestIndex = -1;
    
    var updateView = function(testView, test) {
        testView.setTitle(l10n.testText + " " + (test.index + 1));
        testView.setEditableText(test.inputTest, test.outputTest);
        testView.toggleEditing((test.saveLoadState === Test.State.Saved || test.saveLoadState === Test.State.NotSaved) &&
                                test.removeState !== Test.RemoveState.Removing);
        testView.toggleSaveThrobber(test.saveLoadState === Test.State.Saving);
        testView.toggleLoadThrobber(test.saveLoadState === Test.State.Loading);
        testView.toggleRemoveThrobber(test.removeState === Test.RemoveState.Removing);
        switch (test.error) {
            case Test.Error.LoadError:
                testView.showError(l10n.loadErrorText);
                break;
            case Test.Error.SaveError:
                testView.showError(l10n.saveErrorText);
                break;
            case Test.Error.RemoveError:
                testView.showError(l10n.removeErrorText);
                break;                    
            default:
                testView.hideError();
                break;
        }
        testView.toggleRemoveButton(test.saveLoadState !== Test.State.Saving &&
                                    test.saveLoadState !== Test.State.Loading &&
                                    test.removeState !== Test.RemoveState.Removing);
        testView.toggleSaveLoadButton(test.saveLoadState === Test.State.NotSaved ||
                                      test.error === Test.Error.LoadError);
        switch (test.saveLoadState) {
            case Test.State.Saved:
                testView.setSaveLoadButtonText(l10n.savedText);
                break;
            case Test.State.NotSaved:
                testView.setSaveLoadButtonText(l10n.saveText);
                break;
            case Test.State.Saving:
                testView.setSaveLoadButtonText(l10n.savingText);
                break;
            case Test.State.NotLoaded:
                if (test.error === Test.Error.LoadError) {
                    testView.setSaveLoadButtonText(l10n.tryAgainText);
                } else {
                    testView.setSaveLoadButtonText(l10n.savedText);
                }
                break;
            case Test.State.Loading:
                testView.setSaveLoadButtonText(l10n.savedText);
                break;
        }
    };
    
    var loadTest = function(iTest) {
        var test = tests[iTest];
        var testView = testViews[iTest];
        if (test.saveLoadState !== Test.State.NotLoaded) {
            return;
        }
        test.saveLoadState = Test.State.Loading;
        test.error = Test.Error.NoError;
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
                test.error = Test.Error.NoError;
                test.saveLoadState = Test.State.Saved;
                updateView(testView, test);
                testView.expand();
            }
        ).fail(function() {
            test.error = Test.Error.LoadError;
            test.saveLoadState = Test.State.NotLoaded;
            updateView(testView, test);
        });
    };
    
    var saveTest = function(iTest) {
        var test = tests[iTest];
        var testView = testViews[iTest];
        if (test.saveLoadState !== Test.State.NotSaved) {
            return;
        }
        test.saveLoadState = Test.State.Saving;
        test.error = Test.Error.NoError;
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
                test.error = Test.Error.NoError;
                test.saveLoadState = Test.State.Saved;
                updateView(testView, test);
            }
        ).fail(function() {
            test.error = Test.Error.SaveError;
            test.saveLoadState = Test.State.NotSaved;
            updateView(testView, test);
        });
    };
    
    var addTest = function() {
        $("#addError").addClass("hidden");
        $("#throbberAdd").removeClass("hidden");
        $("#addTest").prop("disabled", true);
        $.getJSON(
            "problems.do",
            {
                reqCode: "addTest"
            },
            function(json) {
                var test = new Test();
                test.index = tests.length;
                test.identifier = json.testId;
                tests.push(test);

                var testView = new TestView(template.clone());
                template.parent().append(testView.getView());        
                testView.onExpand = onTestViewExpand;
                testView.onEdit = onTestViewEdit;
                testView.onSave = onSaveTest;
                testView.onRemove = onRemoveTest;
                testViews.push(testView);
                updateView(testView, test);
                testView.expand();

                $("#throbberAdd").addClass("hidden");
                $("#addTest").prop("disabled", false);
            }
        ).fail(function() {
            $("#throbberAdd").addClass("hidden");
            $("#addTest").prop("disabled", false);
            $("#addError").removeClass("hidden");
        });
    };
    
    var removeTest = function() {
        var test = tests[selectedTestIndex];
        var testView = testViews[selectedTestIndex];
        test.error = Test.Error.NoError;
        test.removeState = Test.RemoveState.Removing;
        updateView(testView, test);
        
        $.post(
            "problems.do",
            {
                reqCode: "deleteTest",
                testId: test.identifier
            },
            function() {
                testView.getView().remove();
                tests.splice(selectedTestIndex, 1);
                testViews.splice(selectedTestIndex, 1);
                for (var iTest = selectedTestIndex; iTest < tests.length; iTest++) {
                    var shiftedTest = tests[iTest];
                    var shiftedTestView = testViews[iTest];
                    shiftedTest.index--;
                    updateView(shiftedTestView, shiftedTest);
                }
            }
        ).fail(function() {
            test.error = Test.Error.RemoveError;
            test.removeState = Test.RemoveState.CantRemove;
            updateView(testView, test);
        });
    };

    var onSaveTest = function(testView) {
        var iTest = testViews.indexOf(testView);
        if (iTest < 0) {
            return;
        }
        var test = tests[iTest];
        switch (test.saveLoadState) {
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
        selectedTestIndex = iTest;
        $("#testIndex").text(selectedTestIndex + 1);
        $("#removeDialog").modal("show");
    };
    
    var onTestViewExpand = function(testView) {
        var iTest = testViews.indexOf(testView);
        if (iTest < 0) {
            return;
        }
        if (tests[iTest].saveLoadState !== Test.State.NotLoaded) {
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
        test.saveLoadState = Test.State.NotSaved;
        test.inputTest = testView.getInputText();
        test.outputTest = testView.getOutputText();
        
        updateView(testView, test);
    };
    
    var onReturn = function(e) {
        var isNotSaved = false;
        for (var iTest = 0; iTest < tests.length; iTest++) {
            var test = tests[iTest];
            if (test.saveLoadState === Test.State.NotSaved ||
                test.saveLoadState === Test.State.Saving) {
                isNotSaved = true;
                break;
            }
        }
        if (isNotSaved) {
            $("#confirmReturn").modal("show");
            e.preventDefault();
        }
    };
    
    for (var iTest = 0; iTest < testList.length; iTest++) {
        var test = new Test();
        test.saveLoadState = Test.State.NotLoaded;
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
    
    $("#addTest").click(addTest);
    $("#removeTest").click(removeTest);
    $(".dudge-tests-return").click(onReturn);
};
