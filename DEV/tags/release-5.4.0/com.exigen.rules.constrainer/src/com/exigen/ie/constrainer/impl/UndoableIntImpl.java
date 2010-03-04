package com.exigen.ie.constrainer.impl;

import com.exigen.ie.constrainer.Constrainer;
import com.exigen.ie.constrainer.Undo;
import com.exigen.ie.constrainer.UndoImpl;
import com.exigen.ie.constrainer.Undoable;
import com.exigen.ie.constrainer.UndoableInt;
import com.exigen.ie.tools.Reusable;
import com.exigen.ie.tools.ReusableFactory;

/**
 * A generic implementation of the UndoableInt.
 */
// public final class UndoableIntImpl extends UndoableOnceImpl implements
// UndoableInt
public final class UndoableIntImpl extends UndoableImpl implements UndoableInt {
    /**
     * Undo Class for UndoUndoableInt.
     */
    static class UndoUndoableInt extends UndoImpl {

        static ReusableFactory _factory = new ReusableFactory() {
            @Override
            protected Reusable createNewElement() {
                return new UndoUndoableInt();
            }

        };

        private int _value;

        static UndoUndoableInt getUndo() {
            return (UndoUndoableInt) _factory.getElement();
        }

        /**
         * Returns a String representation of this object.
         *
         * @return a String representation of this object.
         */
        @Override
        public String toString() {
            return "UndoUndoableInt " + undoable();
        }

        @Override
        public void undo() {
            UndoableIntImpl var = (UndoableIntImpl) undoable();
            var._value = _value;
            super.undo();
        }

        @Override
        public void undoable(Undoable u) {
            super.undoable(u);
            UndoableInt var = (UndoableInt) u;
            _value = var.value();
        }

    } // ~UndoUndoableInt

    private int _value;

    /**
     * Constructor with a given value.
     */
    public UndoableIntImpl(Constrainer constrainer, int value) {
        this(constrainer, value, "");
    }

    /**
     * Constructor with a given value and name.
     */
    public UndoableIntImpl(Constrainer constrainer, int value, String name) {
        super(constrainer, name);
        _value = value;
    }

    public Undo createUndo() {
        return UndoUndoableInt.getUndo();
    }

    /**
     * Sets the current value.
     */
    void forceValue(int value) {
        _value = value;
    }

    public void setValue(int value) {
        if (value != _value) {
            addUndo();
            _value = value;
        }
    }

    /**
     * Returns a String representation of this object.
     *
     * @return a String representation of this object.
     */
    @Override
    public String toString() {
        return name() + "[" + _value + "]";
    }

    public int value() {
        return _value;
    }

} // ~UndoableIntImpl
