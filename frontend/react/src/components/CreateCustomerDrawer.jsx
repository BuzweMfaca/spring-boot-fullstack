import {
    Button,
    Drawer,
    DrawerBody,
    DrawerCloseButton,
    DrawerContent, DrawerFooter,
    DrawerHeader,
    DrawerOverlay,
    useDisclosure
} from "@chakra-ui/react";
import CreateCustomerForm from "./CreateCustomerForm.jsx";

const AddIcon = () => '+';

const CreateCustomerDrawer = ({fetchCustomers}) => {
    const {isOpen, onOpen, onClose} = useDisclosure();
    return(
        <div>
            <Button leftIcon={<AddIcon/>}
                colorScheme={'teal'}
                onClick={onOpen}
            >
                Create customer
            </Button>

            <Drawer isOpen={isOpen} onClose={onClose}>
                <DrawerOverlay />
                <DrawerContent>
                    <DrawerCloseButton />
                    <DrawerHeader>Create new customer</DrawerHeader>

                    <DrawerBody>
                        <CreateCustomerForm
                            fetchCustomers={fetchCustomers}
                        />
                    </DrawerBody>

                    <DrawerFooter>
                        <Button colorScheme={'teal'}
                                onClick={onClose}
                        >
                            Close
                        </Button>
                    </DrawerFooter>
                </DrawerContent>
            </Drawer>

        </div>
    )
}

export default CreateCustomerDrawer;