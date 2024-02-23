import React, {useState} from "react";
import {Exception} from "../../model/Exception";
import {Dialog, DialogContent} from "../../style/Dialog";

interface  ExceptionProps {
    isOpen: boolean;
    exception: Exception;
    onClose: () => void;
}

const ExceptionComponent: React.FC<ExceptionProps> = (props) => {

    return(
        <div>
            {props.isOpen && (
                <Dialog>
                    <DialogContent>
                        <h2>Error</h2>
                        <p>{props.exception.message}</p>
                        <button onClick={props.onClose}>Close</button>
                    </DialogContent>
                </Dialog>
            )}
        </div>
    );
}

export default ExceptionComponent;