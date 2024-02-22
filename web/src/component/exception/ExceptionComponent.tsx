import React, {useState} from "react";
import {Exception} from "../../model/Exception";

interface  ExceptionProps {
    isOpen: boolean;
    exception: Exception;
    onClose: () => void;
}
const ExceptionComponent: React.FC<ExceptionProps> = (props) => {
    const [isOpen, setIsOpen] = useState(props.isOpen);

    return(
        <div>
        </div>
    );
}

export default ExceptionComponent;