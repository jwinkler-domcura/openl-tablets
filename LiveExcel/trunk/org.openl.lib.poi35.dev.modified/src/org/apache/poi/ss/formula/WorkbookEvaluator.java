/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.ss.formula;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.poi.hssf.record.formula.Area3DPtg;
import org.apache.poi.hssf.record.formula.AreaErrPtg;
import org.apache.poi.hssf.record.formula.AreaPtg;
import org.apache.poi.hssf.record.formula.AttrPtg;
import org.apache.poi.hssf.record.formula.BoolPtg;
import org.apache.poi.hssf.record.formula.ControlPtg;
import org.apache.poi.hssf.record.formula.DeletedArea3DPtg;
import org.apache.poi.hssf.record.formula.DeletedRef3DPtg;
import org.apache.poi.hssf.record.formula.ErrPtg;
import org.apache.poi.hssf.record.formula.ExpPtg;
import org.apache.poi.hssf.record.formula.FuncVarPtg;
import org.apache.poi.hssf.record.formula.IntPtg;
import org.apache.poi.hssf.record.formula.MemErrPtg;
import org.apache.poi.hssf.record.formula.MemFuncPtg;
import org.apache.poi.hssf.record.formula.MissingArgPtg;
import org.apache.poi.hssf.record.formula.NamePtg;
import org.apache.poi.hssf.record.formula.NameXPtg;
import org.apache.poi.hssf.record.formula.NumberPtg;
import org.apache.poi.hssf.record.formula.OperationPtg;
import org.apache.poi.hssf.record.formula.Ptg;
import org.apache.poi.hssf.record.formula.Ref3DPtg;
import org.apache.poi.hssf.record.formula.RefErrorPtg;
import org.apache.poi.hssf.record.formula.RefPtg;
import org.apache.poi.hssf.record.formula.StringPtg;
import org.apache.poi.hssf.record.formula.UnionPtg;
import org.apache.poi.hssf.record.formula.UnknownPtg;
import org.apache.poi.hssf.record.formula.eval.AreaEval;
import org.apache.poi.hssf.record.formula.eval.BlankEval;
import org.apache.poi.hssf.record.formula.eval.BoolEval;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
import org.apache.poi.hssf.record.formula.eval.MissingArgEval;
import org.apache.poi.hssf.record.formula.eval.NameEval;
import org.apache.poi.hssf.record.formula.eval.NameXEval;
import org.apache.poi.hssf.record.formula.eval.NumberEval;
import org.apache.poi.hssf.record.formula.eval.OperationEval;
import org.apache.poi.hssf.record.formula.eval.RefEval;
import org.apache.poi.hssf.record.formula.eval.StringEval;
import org.apache.poi.hssf.record.formula.eval.ValueEval;
import org.apache.poi.hssf.record.formula.functions.FreeRefFunction;
import org.apache.poi.hssf.record.formula.udf.UDFFinder;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.formula.CollaboratingWorkbooksEnvironment.WorkbookNotFoundException;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.apache.poi.ss.usermodel.Cell;

/**
 * Evaluates formula cells.<p/>
 *
 * For performance reasons, this class keeps a cache of all previously calculated intermediate
 * cell values.  Be sure to call {@link #clearAllCachedResultValues()} if any workbook cells are changed between
 * calls to evaluate~ methods on this class.<br/>
 *
 * For POI internal use only
 *
 * @author Josh Micich
 * 
 * Modified 09/07/09 by Petr Udalau - Expanded working with OperationEvaluationContext and plain cached values.
 * Added method setCachedCellValue(EvaluationCell, ValueEval, EvaluationTracker).
 * Added method evaluate(FreeRefFunction, ValueEval[]). 
 */
public final class WorkbookEvaluator {

	private final EvaluationWorkbook _workbook;
	private EvaluationCache _cache;
	/** part of cache entry key (useful when evaluating multiple workbooks) */
	private int _workbookIx;

	private final IEvaluationListener _evaluationListener;
	private final Map<EvaluationSheet, Integer> _sheetIndexesBySheet;
	private final Map<String, Integer> _sheetIndexesByName;
	private CollaboratingWorkbooksEnvironment _collaboratingWorkbookEnvironment;
	private final IStabilityClassifier _stabilityClassifier;
	private final UDFFinder _udfFinder;

	/**
	 * @param udfFinder pass <code>null</code> for default (AnalysisToolPak only)
	 */
	public WorkbookEvaluator(EvaluationWorkbook workbook, IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
		this (workbook, null, stabilityClassifier, udfFinder);
	}
	/* package */ WorkbookEvaluator(EvaluationWorkbook workbook, IEvaluationListener evaluationListener,
			IStabilityClassifier stabilityClassifier, UDFFinder udfFinder) {
		_workbook = workbook;
		_evaluationListener = evaluationListener;
		_cache = new EvaluationCache(evaluationListener);
		_sheetIndexesBySheet = new IdentityHashMap<EvaluationSheet, Integer>();
		_sheetIndexesByName = new IdentityHashMap<String, Integer>();
		_collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
		_workbookIx = 0;
		_stabilityClassifier = stabilityClassifier;
		_udfFinder = udfFinder == null ? UDFFinder.DEFAULT : udfFinder;
	}

	/**
	 * also for debug use. Used in toString methods
	 */
	/* package */ String getSheetName(int sheetIndex) {
		return _workbook.getSheetName(sheetIndex);
	}

	/* package */ EvaluationSheet getSheet(int sheetIndex) {
		return _workbook.getSheet(sheetIndex);
	}

	private static boolean isDebugLogEnabled() {
		return false;
	}
	private static void logDebug(String s) {
		if (isDebugLogEnabled()) {
			System.out.println(s);
		}
	}
	/* package */ void attachToEnvironment(CollaboratingWorkbooksEnvironment collaboratingWorkbooksEnvironment, EvaluationCache cache, int workbookIx) {
		_collaboratingWorkbookEnvironment = collaboratingWorkbooksEnvironment;
		_cache = cache;
		_workbookIx = workbookIx;
	}
	/* package */ CollaboratingWorkbooksEnvironment getEnvironment() {
		return _collaboratingWorkbookEnvironment;
	}

	/**
	 * Discards the current workbook environment and attaches to the default 'empty' environment.
	 * Also resets evaluation cache.
	 */
	/* package */ void detachFromEnvironment() {
		_collaboratingWorkbookEnvironment = CollaboratingWorkbooksEnvironment.EMPTY;
		_cache = new EvaluationCache(_evaluationListener);
		_workbookIx = 0;
	}
	/**
	 * @return the evaluator for another workbook which is part of the same {@link CollaboratingWorkbooksEnvironment}
	 */
	/* package */ WorkbookEvaluator getOtherWorkbookEvaluator(String workbookName) throws WorkbookNotFoundException {
		return _collaboratingWorkbookEnvironment.getWorkbookEvaluator(workbookName);
	}

	/* package */ IEvaluationListener getEvaluationListener() {
		return _evaluationListener;
	}

	/**
	 * Should be called whenever there are changes to input cells in the evaluated workbook.
	 * Failure to call this method after changing cell values will cause incorrect behaviour
	 * of the evaluate~ methods of this class
	 */
	public void clearAllCachedResultValues() {
		_cache.clear();
		_sheetIndexesBySheet.clear();
	}

    /**
     * Sets new value of cell into cache(only cache).
     */
    public void setCachedCellValue(EvaluationCell cell, ValueEval newValue) {
        CellCacheEntry cce = null;
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            cce = _cache.getOrCreateFormulaCellEntry(cell);
        } else {
           cce = _cache.getOrCreatePlainValueEntry(_workbookIx, getSheetIndex(cell.getSheet()), cell.getRowIndex(),
                    cell.getColumnIndex());
        }
        cce.updateValue(newValue);
        cce.recurseClearCachedFormulaResults(null);
    }

	/**
	 * Should be called to tell the cell value cache that the specified (value or formula) cell
	 * has changed.
	 */
	public void notifyUpdateCell(EvaluationCell cell) {
		int sheetIndex = getSheetIndex(cell.getSheet());
		_cache.notifyUpdateCell(_workbookIx, sheetIndex, cell);
	}
	/**
	 * Should be called to tell the cell value cache that the specified cell has just been
	 * deleted.
	 */
	public void notifyDeleteCell(EvaluationCell cell) {
		int sheetIndex = getSheetIndex(cell.getSheet());
		_cache.notifyDeleteCell(_workbookIx, sheetIndex, cell);
	}

	private int getSheetIndex(EvaluationSheet sheet) {
		Integer result = _sheetIndexesBySheet.get(sheet);
		if (result == null) {
			int sheetIndex = _workbook.getSheetIndex(sheet);
			if (sheetIndex < 0) {
				throw new RuntimeException("Specified sheet from a different book");
			}
			result = new Integer(sheetIndex);
			_sheetIndexesBySheet.put(sheet, result);
		}
		return result.intValue();
	}

	public ValueEval evaluate(EvaluationCell srcCell) {
		int sheetIndex = getSheetIndex(srcCell.getSheet());
		return evaluateAny(srcCell, sheetIndex, srcCell.getRowIndex(), srcCell.getColumnIndex(), new EvaluationTracker());
	}

	/**
	 * Case-insensitive.
	 * @return -1 if sheet with specified name does not exist
	 */
	/* package */ int getSheetIndex(String sheetName) {
		Integer result = _sheetIndexesByName.get(sheetName);
		if (result == null) {
			int sheetIndex = _workbook.getSheetIndex(sheetName);
			if (sheetIndex < 0) {
				return -1;
			}
			result = new Integer(sheetIndex);
			_sheetIndexesByName.put(sheetName, result);
		}
		return result.intValue();
	}


	/**
	 * @return never <code>null</code>, never {@link BlankEval}
	 */
	private ValueEval evaluateAny(EvaluationCell srcCell, int sheetIndex, int rowIndex, int columnIndex,
	        EvaluationTracker tracker) {
        // avoid tracking dependencies for cells that have constant definition
        boolean shouldCellDependencyBeRecorded = _stabilityClassifier == null ? true : !_stabilityClassifier
                .isCellFinal(sheetIndex, rowIndex, columnIndex);
        CellCacheEntry result = null;
        if (srcCell == null || srcCell.getCellType() != Cell.CELL_TYPE_FORMULA) {
            result = getNonFormulaCellValue(srcCell, sheetIndex, rowIndex, columnIndex, tracker);
        } else {
            result = evaluateFormulaCell(srcCell, sheetIndex, rowIndex, columnIndex, tracker);
        }

        if (shouldCellDependencyBeRecorded) {
            tracker.acceptDependency(_workbookIx, sheetIndex, rowIndex, columnIndex, result);
        }

        return result.getValue();
    }

    private CellCacheEntry getNonFormulaCellValue(EvaluationCell srcCell, int sheetIndex, int rowIndex, int columnIndex,
            EvaluationTracker tracker) {
        PlainValueCellCacheEntry entry = _cache.getOrCreatePlainValueEntry(_workbookIx, sheetIndex, rowIndex,
                columnIndex);
        if (entry.getValue() == null) {
            entry.updateValue(getValueFromNonFormulaCell(srcCell));
        }
        return entry;
    }

    private CellCacheEntry evaluateFormulaCell(EvaluationCell srcCell, int sheetIndex, int rowIndex, int columnIndex,
            EvaluationTracker tracker) {
        FormulaCellCacheEntry cce = _cache.getOrCreateFormulaCellEntry(srcCell);
        IEvaluationListener evalListener = _evaluationListener;
        if (cce.getValue() == null) {
            if (!tracker.startEvaluate(cce)) {
                cce.updateValue(ErrorEval.CIRCULAR_REF_ERROR);
            } else{
                OperationEvaluationContext ec = new OperationEvaluationContext(this, _workbook, sheetIndex, rowIndex, columnIndex, tracker);
                try {
                    ValueEval result;
                    Ptg[] ptgs = _workbook.getFormulaTokens(srcCell);
                    if (evalListener == null) {
                        result = evaluateFormula(ec, ptgs);
                    } else {
                        evalListener.onStartEvaluate(srcCell, cce, ptgs);
                        result = evaluateFormula(ec, ptgs);
                        evalListener.onEndEvaluate(cce, result);
                    }

                    tracker.updateCacheResult(result);
                } catch (NotImplementedException e) {
                    throw addExceptionInfo(e, sheetIndex, rowIndex, columnIndex);
                } finally {
                    tracker.endEvaluate(cce);
                    ec.rollBackTemporaryCells();
                }
            }
        } else {
            if(evalListener != null) {
                evalListener.onCacheHit(sheetIndex, rowIndex, columnIndex, cce.getValue());
            }
        }
        if (isDebugLogEnabled()) {
            String sheetName = getSheetName(sheetIndex);
            CellReference cr = new CellReference(rowIndex, columnIndex);
            logDebug("Evaluated " + sheetName + "!" + cr.formatAsString() + " to " + cce.getValue().toString());
        }
        return cce;
    }

	/**
	 * Adds the current cell reference to the exception for easier debugging.
	 * Would be nice to get the formula text as well, but that seems to require
	 * too much digging around and casting to get the FormulaRenderingWorkbook.
	 */
	private NotImplementedException addExceptionInfo(NotImplementedException inner, int sheetIndex, int rowIndex, int columnIndex) {

		try {
			String sheetName = _workbook.getSheetName(sheetIndex);
			CellReference cr = new CellReference(sheetName, rowIndex, columnIndex, false, false);
			String msg =  "Error evaluating cell " + cr.formatAsString();
			return new NotImplementedException(msg, inner);
		} catch (Exception e) {
			// avoid bombing out during exception handling
			e.printStackTrace();
			return inner; // preserve original exception
		}
	}
	/**
	 * Gets the value from a non-formula cell.
	 * @param cell may be <code>null</code>
	 * @return {@link BlankEval} if cell is <code>null</code> or blank, never <code>null</code>
	 */
	/* package */ static ValueEval getValueFromNonFormulaCell(EvaluationCell cell) {
		if (cell == null) {
			return BlankEval.INSTANCE;
		}
		int cellType = cell.getCellType();
		switch (cellType) {
			case Cell.CELL_TYPE_NUMERIC:
				return new NumberEval(cell.getNumericCellValue());
			case Cell.CELL_TYPE_STRING:
				return new StringEval(cell.getStringCellValue());
			case Cell.CELL_TYPE_BOOLEAN:
				return BoolEval.valueOf(cell.getBooleanCellValue());
			case Cell.CELL_TYPE_BLANK:
				return BlankEval.INSTANCE;
			case Cell.CELL_TYPE_ERROR:
				return ErrorEval.valueOf(cell.getErrorCellValue());
		}
		throw new RuntimeException("Unexpected cell type (" + cellType + ")");
	}
	// visibility raised for testing
	/* package */ ValueEval evaluateFormula(OperationEvaluationContext ec, Ptg[] ptgs) {

		Stack<ValueEval> stack = new Stack<ValueEval>();
		for (int i = 0, iSize = ptgs.length; i < iSize; i++) {

			// since we don't know how to handle these yet :(
			Ptg ptg = ptgs[i];
			if (ptg instanceof AttrPtg) {
				AttrPtg attrPtg = (AttrPtg) ptg;
				if (attrPtg.isSum()) {
					// Excel prefers to encode 'SUM()' as a tAttr token, but this evaluator
					// expects the equivalent function token
					byte nArgs = 1;  // tAttrSum always has 1 parameter
					ptg = new FuncVarPtg("SUM", nArgs);
				}
			}
			if (ptg instanceof ControlPtg) {
				// skip Parentheses, Attr, etc
				continue;
			}
			if (ptg instanceof MemFuncPtg) {
				// can ignore, rest of tokens for this expression are in OK RPN order
				continue;
			}
			if (ptg instanceof MemErrPtg) { continue; }

			ValueEval opResult;
			if (ptg instanceof OperationPtg) {
				OperationPtg optg = (OperationPtg) ptg;

				if (optg instanceof UnionPtg) { continue; }

				OperationEval operation = OperationEvaluatorFactory.create(optg);

				int numops = operation.getNumberOfOperands();
				ValueEval[] ops = new ValueEval[numops];

				// storing the ops in reverse order since they are popping
				for (int j = numops - 1; j >= 0; j--) {
					ValueEval p = stack.pop();
					ops[j] = p;
				}
//				logDebug("invoke " + operation + " (nAgs=" + numops + ")");
				opResult = operation.evaluate(ops, ec);
				if (opResult == MissingArgEval.instance) {
					opResult = BlankEval.INSTANCE;
				}
			} else {
				opResult = getEvalForPtg(ptg, ec);
			}
			if (opResult == null) {
				throw new RuntimeException("Evaluation result must not be null");
			}
//			logDebug("push " + opResult);
			stack.push(opResult);
		}

		ValueEval value = stack.pop();
		if (!stack.isEmpty()) {
			throw new IllegalStateException("evaluation stack not empty");
		}
		value = dereferenceValue(value, ec.getRowIndex(), ec.getColumnIndex());
		if (value == BlankEval.INSTANCE) {
			// Note Excel behaviour here. A blank final final value is converted to zero.
			return NumberEval.ZERO;
			// Formulas _never_ evaluate to blank.  If a formula appears to have evaluated to
			// blank, the actual value is empty string. This can be verified with ISBLANK().
		}
		return value;
	}

	/**
	 * Dereferences a single value from any AreaEval or RefEval evaluation result.
	 * If the supplied evaluationResult is just a plain value, it is returned as-is.
	 * @return a <tt>NumberEval</tt>, <tt>StringEval</tt>, <tt>BoolEval</tt>,
	 *  <tt>BlankEval</tt> or <tt>ErrorEval</tt>. Never <code>null</code>.
	 */
	private static ValueEval dereferenceValue(ValueEval evaluationResult, int srcRowNum, int srcColNum) {
		if (evaluationResult instanceof RefEval) {
			RefEval rv = (RefEval) evaluationResult;
			return rv.getInnerValueEval();
		}
		if (evaluationResult instanceof AreaEval) {
			AreaEval ae = (AreaEval) evaluationResult;
			if (ae.isRow()) {
				if(ae.isColumn()) {
					return ae.getRelativeValue(0, 0);
				}
				return ae.getValueAt(ae.getFirstRow(), srcColNum);
			}
			if (ae.isColumn()) {
				return ae.getValueAt(srcRowNum, ae.getFirstColumn());
			}
			return ErrorEval.VALUE_INVALID;
		}
		return evaluationResult;
	}


	/**
	 * returns an appropriate Eval impl instance for the Ptg. The Ptg must be
	 * one of: Area3DPtg, AreaPtg, ReferencePtg, Ref3DPtg, IntPtg, NumberPtg,
	 * StringPtg, BoolPtg <br/>special Note: OperationPtg subtypes cannot be
	 * passed here!
	 */
	private ValueEval getEvalForPtg(Ptg ptg, OperationEvaluationContext ec) {
		//  consider converting all these (ptg instanceof XxxPtg) expressions to (ptg.getClass() == XxxPtg.class)

		if (ptg instanceof NamePtg) {
			// named ranges, macro functions
			NamePtg namePtg = (NamePtg) ptg;
			EvaluationName nameRecord = _workbook.getName(namePtg);
			if (nameRecord.isFunctionName()) {
				return new NameEval(nameRecord.getNameText());
			}
			if (nameRecord.hasFormula()) {
				return evaluateNameFormula(nameRecord.getNameDefinition(), ec);
			}

			throw new RuntimeException("Don't now how to evalate name '" + nameRecord.getNameText() + "'");
		}
		if (ptg instanceof NameXPtg) {
			return new NameXEval(((NameXPtg) ptg));
		}

		if (ptg instanceof IntPtg) {
			return new NumberEval(((IntPtg)ptg).getValue());
		}
		if (ptg instanceof NumberPtg) {
			return new NumberEval(((NumberPtg)ptg).getValue());
		}
		if (ptg instanceof StringPtg) {
			return new StringEval(((StringPtg) ptg).getValue());
		}
		if (ptg instanceof BoolPtg) {
			return BoolEval.valueOf(((BoolPtg) ptg).getValue());
		}
		if (ptg instanceof ErrPtg) {
			return ErrorEval.valueOf(((ErrPtg) ptg).getErrorCode());
		}
		if (ptg instanceof MissingArgPtg) {
			return MissingArgEval.instance;
		}
		if (ptg instanceof AreaErrPtg ||ptg instanceof RefErrorPtg
				|| ptg instanceof DeletedArea3DPtg || ptg instanceof DeletedRef3DPtg) {
				return ErrorEval.REF_INVALID;
		}
		if (ptg instanceof Ref3DPtg) {
			Ref3DPtg refPtg = (Ref3DPtg) ptg;
			SheetRefEvaluator sre = ec.createExternSheetRefEvaluator(refPtg);
			return new LazyRefEval(refPtg, sre);
		}
		if (ptg instanceof Area3DPtg) {
			Area3DPtg aptg = (Area3DPtg) ptg;
			SheetRefEvaluator sre = ec.createExternSheetRefEvaluator(aptg);
			return new LazyAreaEval(aptg, sre);
		}
		SheetRefEvaluator sre = ec.getRefEvaluatorForCurrentSheet();
		if (ptg instanceof RefPtg) {
			return new LazyRefEval(((RefPtg) ptg), sre);
		}
		if (ptg instanceof AreaPtg) {
			return new LazyAreaEval(((AreaPtg) ptg), sre);
		}

		if (ptg instanceof UnknownPtg) {
			// POI uses UnknownPtg when the encoded Ptg array seems to be corrupted.
			// This seems to occur in very rare cases (e.g. unused name formulas in bug 44774, attachment 21790)
			// In any case, formulas are re-parsed before execution, so UnknownPtg should not get here
			throw new RuntimeException("UnknownPtg not allowed");
		}
		if (ptg instanceof ExpPtg) {
			// ExpPtg is used for array formulas and shared formulas.
			// it is currently unsupported, and may not even get implemented here
			throw new RuntimeException("ExpPtg currently not supported");
		}

		throw new RuntimeException("Unexpected ptg class (" + ptg.getClass().getName() + ")");
	}
	private ValueEval evaluateNameFormula(Ptg[] ptgs, OperationEvaluationContext ec) {
		if (ptgs.length > 1) {
			throw new RuntimeException("Complex name formulas not supported yet");
		}
		return getEvalForPtg(ptgs[0], ec);
	}

	/**
	 * Used by the lazy ref evals whenever they need to get the value of a contained cell.
	 */
	/* package */ ValueEval evaluateReference(EvaluationSheet sheet, int sheetIndex, int rowIndex,
			int columnIndex, EvaluationTracker tracker) {

		EvaluationCell cell = sheet.getCell(rowIndex, columnIndex);
		return evaluateAny(cell, sheetIndex, rowIndex, columnIndex, tracker);
	}
	public FreeRefFunction findUserDefinedFunction(String functionName) {
		return _udfFinder.findFunction(functionName);
	}
}
